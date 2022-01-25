package br.com.zup.ZupNotion.services;


import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import br.com.zup.ZupNotion.services.EmailService;
import br.com.zup.ZupNotion.services.SenhaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @BeforeEach
    public void setup(){
        usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("maria@zup.com.br");
        usuario.setSenha("123mariA@");
    }

    @Test
    public void testarCadastrarUsuario(){
        usuarioService.cadastrarUsuario(usuario);
        InOrder inOrder = Mockito.inOrder(emailService, emailService, senhaService, senhaService);

        inOrder.verify(emailService, Mockito.times(1)).verificarEmailExistente(usuario.getEmail());
        inOrder.verify(emailService, Mockito.times(1)).validarEmailZup(usuario.getEmail());
        inOrder.verify(senhaService, Mockito.times(1)).verificarSenhaForte(usuario.getSenha());
        inOrder.verify(senhaService, Mockito.times(1)).criptografarSenha(usuario);
    }

}