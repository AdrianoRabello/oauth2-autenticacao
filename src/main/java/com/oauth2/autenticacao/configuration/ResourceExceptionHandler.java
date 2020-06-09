package com.oauth2.autenticacao.configuration;

import com.oauth2.autenticacao.dto.ErroDto;
import com.oauth2.autenticacao.dto.FormValidationErrorDto;
import com.oauth2.autenticacao.exception.NegocioException;
import com.oauth2.autenticacao.exception.RegistroNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

public class ResourceExceptionHandler  {

    private MessageSource messageSource;

    @Autowired
    public ResourceExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<FormValidationErrorDto> handle(MethodArgumentNotValidException exception) {
        List<FormValidationErrorDto> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(error -> {
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            FormValidationErrorDto formValidationErrorDto = new FormValidationErrorDto(error.getField(), message);

            errors.add(formValidationErrorDto);
        });

        return errors;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErroDto handle(IllegalArgumentException exception) {
        return new ErroDto(messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()));
    }

    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NegocioException.class)
    public ErroDto handle(NegocioException exception) {
        return new ErroDto(messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()));
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ErroDto handle(RegistroNaoEncontradoException exception) {
        return new ErroDto(messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()));
    }
}