package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public Usuario alterarSenha(Usuario usuario) {
        Usuario usuarioBanco = emailService.localizarPorEmail(usuario.getEmail());
        usuarioBanco.setSenha(criptografarSenha(usuario.getSenha()));
        return usuarioRepository.save(usuarioBanco);
    }

    public String criptografarSenha(String senha){
        return encoder.encode(senha);
    }

}
