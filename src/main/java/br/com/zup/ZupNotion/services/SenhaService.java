package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.SenhaInvalidaException;
import org.springframework.stereotype.Service;

@Service
public class SenhaService {

    public void verificarSenhaForte(String senha) {

        boolean tamanhoAdequado = false;
        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        if (senha.length() >= 6) tamanhoAdequado = true;

        for (char caractere : senha.toCharArray()) {
            if (caractere >= '0' && caractere <= '9') {
                achouNumero = true;
            } else if (caractere >= 'A' && caractere <= 'Z') {
                achouMaiuscula = true;
            } else if (caractere >= 'a' && caractere <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        if (!tamanhoAdequado || !achouNumero || !achouMaiuscula || !achouMinuscula || !achouSimbolo) {
            throw new SenhaInvalidaException("Senha inválida");
        }

    }
}
