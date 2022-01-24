package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.usuario.Usuario;
import br.com.zup.ZupNotion.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SenhaService senhaService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void cadastrarUsuario(Usuario usuario) {
        emailService.verificarEmailNaoExistente(usuario.getEmail());
        emailService.validarEmailZup(usuario.getEmail());
        senhaService.verificarSenhaForte(usuario.getSenha());
        criptografarSenha(usuario);
        usuarioRepository.save(usuario);
    }

    public void criptografarSenha(Usuario usuario){
        String senhaEscondida = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEscondida);
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

}


