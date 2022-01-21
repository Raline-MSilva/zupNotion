package br.com.zup.ZupNotion.Exceptions;

public class EmailJaExistente extends RuntimeException{
    public EmailJaExistente(String message) {
        super(message);
    }

}
