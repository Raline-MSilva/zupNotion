package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.SenhaInvalidaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SenhaServiceTest {

    @Autowired
    private SenhaService senhaService;

    @Test
    public void testandoSenhaComSucesso(){
        Assertions.assertDoesNotThrow(() -> {senhaService.verificarSenhaForte("Aaaa123#");});
    }

    @Test
    public void testandoSenhaComExcecao(){
        SenhaInvalidaException exception = Assertions.assertThrows(SenhaInvalidaException.class, ()-> {
            senhaService.verificarSenhaForte("abc$");
        });
        Assertions.assertEquals("Senha inválida", exception.getMessage());
    }

}
