package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.DominioInvalidoException;
import br.com.zup.ZupNotion.exceptions.EmailJaExistenteException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        DominioInvalidoException exception = Assertions.assertThrows(DominioInvalidoException.class, ()-> {
            emailService.validarEmailZup(email);
        });
        Assertions.assertEquals("Permitido cadastro apenas para email Zup!", exception.getMessage());
    }

    @Test
    public void testarVerificarEmailExistente(){
        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);
        EmailJaExistenteException exception = Assertions.assertThrows(EmailJaExistenteException.class, () -> {
            emailService.verificarEmailExistente(usuario.getEmail());
        });
        Assertions.assertEquals("Email já cadastrado!", exception.getMessage());
    }

}
