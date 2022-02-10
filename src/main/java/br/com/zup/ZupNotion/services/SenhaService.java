package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public Usuario alterarSenha(Usuario usuario) {
        Usuario usuarioBanco = emailService.localizarUsuarioPorEmail(usuario.getEmail());
        if (Objects.equals(usuarioBanco.getPerguntaDeSeguranca(), usuario.getPerguntaDeSeguranca()) &&
                Objects.equals(usuarioBanco.getRespostaDeSeguranca(), usuario.getRespostaDeSeguranca())) {
            usuarioBanco.setSenha(criptografarSenha(usuario.getSenha()));
            return usuarioRepository.save(usuarioBanco);
        }
        throw new RuntimeException("dados inválidos");
    }

    public String criptografarSenha(String senha){
        return encoder.encode(senha);
    }

}
