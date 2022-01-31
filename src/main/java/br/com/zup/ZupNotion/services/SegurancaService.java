package br.com.zup.ZupNotion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SegurancaService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    public String criptografarSenha(String senha){
        return encoder.encode(senha);
    }

}
