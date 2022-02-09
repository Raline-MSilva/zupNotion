package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.config.security.JWT.UsuarioLogado;
import br.com.zup.ZupNotion.exceptions.PerfilInvalidoException;
import br.com.zup.ZupNotion.exceptions.RoleInvalidoParaEssaRequisicao;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Role;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
        usuario.setRole(Role.ROLE_USER);
        usuario.setSenha(senhaService.criptografarSenha(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario cadastrarAdmin(Usuario usuario) {
        if (buscarUsuarioLogado().getRole() == Role.ROLE_ADMIN) {
            emailService.verificarEmailExistente(usuario.getEmail());
            emailService.validarEmailZup(usuario.getEmail());
            usuario.setRole(Role.ROLE_ADMIN);
            usuario.setSenha(senhaService.criptografarSenha(usuario.getSenha()));
            return usuarioRepository.save(usuario);
        }
        throw new PerfilInvalidoException("Tipo de perfil inválido");
    }

    public void verificarRole(String role) {
        if (!Objects.equals(role, "ROLE_ADMIN") && !Objects.equals(role, "ROLE_USER")) {
            throw new PerfilInvalidoException("Tipo de perfil inválido");
        }
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

    public void deletarUsuario(String email) {
        Usuario usuario = emailService.localizarUsuarioPorEmail(email);
        usuarioRepository.delete(usuario);
    }

    public Page<Usuario> buscarUsuarios(Pageable pageable) {
        Usuario usuario = buscarUsuarioLogado();
        if (usuario.getRole() == Role.ROLE_ADMIN) {
            return usuarioRepository.findAll(pageable);
        }
        throw new RoleInvalidoParaEssaRequisicao("Requisição permitida apenas para admin.");
    }

}

