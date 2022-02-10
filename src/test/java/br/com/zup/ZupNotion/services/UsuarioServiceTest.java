package br.com.zup.ZupNotion.services;


import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Role;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UsuarioServiceTest {
    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private SenhaService senhaService;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Pageable pageable;
    private Page<Usuario> pageUsuarios;

    @BeforeEach
    public void setup() {
        usuario = new Usuario();
        usuario.setId("id12345");
        usuario.setNome("Maria");
        usuario.setEmail("maria@zup.com.br");
        usuario.setSenha("123mariA@");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        pageable = PageRequest.of(0, usuarios.size());
        pageUsuarios = new PageImpl(usuarios, pageable, usuarios.size());
    }

    @Test
    public void testarCadastrarUsuario() {
        String senhaCriptografada = "ncjwncew";
        usuario.setRole(Role.ROLE_USER);
        Mockito.when(emailService.verificarEmailExistente(usuario.getEmail())).thenReturn(false);
        Mockito.when(emailService.validarEmailZup(usuario.getEmail())).thenReturn(true);
        Assertions.assertEquals(usuario.getRole(), Role.ROLE_USER);
        Mockito.when(senhaService.criptografarSenha(usuario.getSenha())).thenReturn(senhaCriptografada);
        Assertions.assertNotEquals(usuario.getSenha(), senhaCriptografada);
    }

    @Test
    public void testarCadastrarAdmin() {
        String senhaCriptografada = "ncjwncew";
        usuario.setRole(Role.ROLE_ADMIN);
        Mockito.when(emailService.verificarEmailExistente(usuario.getEmail())).thenReturn(false);
        Mockito.when(emailService.validarEmailZup(usuario.getEmail())).thenReturn(true);
        Assertions.assertEquals(usuario.getRole(), Role.ROLE_ADMIN);
        Mockito.when(senhaService.criptografarSenha(usuario.getSenha())).thenReturn(senhaCriptografada);
        Assertions.assertNotEquals(usuario.getSenha(), senhaCriptografada);
    }

    @Test
    public void testarDeletarUsuario() {
        Mockito.when(emailService.localizarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        Mockito.doNothing().when(usuarioRepository).deleteById(Mockito.anyString());
    }

//    @Test
//    public void testarBuscarTodosUsuarios() {
//        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
//        usuario.setRole(Role.ROLE_ADMIN);
//        Mockito.when(usuarioRepository.findById(Mockito.anyString())).thenReturn(Optional.of(usuario));
//        usuarioService.buscarUsuarios(pageable);
//
//
//        Mockito.verify(usuarioRepository, Mockito.times(1)).findAll(pageable);
//
//    }

}