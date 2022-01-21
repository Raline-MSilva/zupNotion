package br.com.zup.ZupNotion.exceptions;

public class EmailJaExistenteException extends RuntimeException{
    public EmailJaExistenteException(String message) {
        super(message);
    }

}
