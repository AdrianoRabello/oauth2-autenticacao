package com.oauth2.autenticacao.exception;

public class RegistroNaoEncontradoException extends RuntimeException {

    private static final String NOT_FOUND = "erro.naoEncontrado";

    public RegistroNaoEncontradoException() {
        super(NOT_FOUND);
    }

    public RegistroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
