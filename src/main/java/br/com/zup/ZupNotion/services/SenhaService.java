package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.SenhaInvalidaException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

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

    public void alterarSenha(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNaoExisteException("Usuário não existe");
        }

        Usuario usuarioBanco = usuarioOptional.get();
        usuarioBanco.setSenha(usuario.getSenha());

        usuarioRepository.save(usuarioBanco);
    }

    public void criptografarSenha(Usuario usuario){
        String senhaEscondida = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEscondida);
    }

}
