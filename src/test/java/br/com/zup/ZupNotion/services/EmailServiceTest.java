package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.DominioInvalidoException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class EmailServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    private Usuario usuario;

    @BeforeEach
    public void setup(){
        usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("maria@zup.com.br");
        usuario.setSenha("123mariA@");
    }

    @Test
    public void testarValidacaoDeEmailZup(){
        boolean eValido = emailService.validarEmailZup(usuario.getEmail());
        Assertions.assertTrue(eValido);
    }

    @Test
    public void testarExcecaoEmailNaoZup(){
        String email = "maria@gmail.com";
        Assertions.assertThrows(DominioInvalidoException.class, ()-> {
            emailService.validarEmailZup(email);
        });
    }

}
