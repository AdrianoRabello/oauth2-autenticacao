package com.oauth2.autenticacao;

public class AutenticacaoException  extends RuntimeException {

    private static final String AUTENTICACAO = "erro.autenticacao";

    public AutenticacaoException() {
        super(AUTENTICACAO);
    }

    public AutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
