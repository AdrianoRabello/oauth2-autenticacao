package com.oauth2.autenticacao.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class ErroDto {

    private String erro;

    public ErroDto(String erro) {
        this.erro = erro;
    }

    public String getErro() {
        return erro;
    }
}
