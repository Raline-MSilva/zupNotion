package br.com.zup.ZupNotion.Exceptions;

public class EmailJaExistenteException extends RuntimeException{
    public EmailJaExistenteException(String message) {
        super(message);
    }

}
