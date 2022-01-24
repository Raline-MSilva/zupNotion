package br.com.zup.ZupNotion.exceptions;

public class UsuarioNaoExisteException extends RuntimeException {
    public UsuarioNaoExisteException(String message) {
        super(message);
    }

}
