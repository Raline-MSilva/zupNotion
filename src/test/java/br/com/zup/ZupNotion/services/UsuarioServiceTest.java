package br.com.zup.ZupNotion.services;


import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
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

    @MockBean
    private SegurancaService segurancaService;

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
        InOrder inOrder = Mockito.inOrder(emailService, emailService, segurancaService);

        inOrder.verify(emailService, Mockito.times(1)).verificarEmailExistente(usuario.getEmail());
        inOrder.verify(emailService, Mockito.times(1)).validarEmailZup(usuario.getEmail());
        inOrder.verify(segurancaService, Mockito.times(1)).criptografarSenha(usuario.getSenha());
    }

}