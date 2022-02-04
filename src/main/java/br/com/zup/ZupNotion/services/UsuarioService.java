package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.config.security.JWT.UsuarioLogado;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Usuario cadastrarUsuario(Usuario usuario) {
        emailService.verificarEmailExistente(usuario.getEmail());
        emailService.validarEmailZup(usuario.getEmail());
        usuario.setRole("ROLE_USER");
        usuario.setSenha(senhaService.criptografarSenha(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public String buscarIdUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsuarioLogado usuarioLogado = (UsuarioLogado) principal;
        return usuarioLogado.getId();
    }

    public Usuario buscarUsuarioLogado() {
        Optional<Usuario> usuario = usuarioRepository.findById(buscarIdUsuarioLogado());
        if (usuario.isPresent()) {
            return usuario.get();
        }
        throw new UsuarioNaoExisteException("Usuário não existe");
    }

}
