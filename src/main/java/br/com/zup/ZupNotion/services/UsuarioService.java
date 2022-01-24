package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SenhaService senhaService;

    public void cadastrarUsuario(Usuario usuario) {
        emailService.verificarEmailExistente(usuario.getEmail());
        emailService.validarEmailZup(usuario.getEmail());
        senhaService.verificarSenhaForte(usuario.getSenha());
        senhaService.criptografarSenha(usuario);
        usuarioRepository.save(usuario);
    }

}
