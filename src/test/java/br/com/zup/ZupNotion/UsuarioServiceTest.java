package br.com.zup.ZupNotion;


import br.com.zup.ZupNotion.usuario.UsuarioRepository;
import br.com.zup.ZupNotion.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.AssertTrue;

@SpringBootTest
public class UsuarioServiceTest {
    @Mock
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void testandoValidacaoDeEmailZup(){
        var validacaoDeEmail =
        usuarioService.validarEmailZup("raline@zup.com.br");
        Assertions.assertTrue(validacaoDeEmail);
    }
}
