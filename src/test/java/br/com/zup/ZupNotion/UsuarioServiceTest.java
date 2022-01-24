package br.com.zup.ZupNotion;


import br.com.zup.ZupNotion.usuario.Usuario;
import br.com.zup.ZupNotion.usuario.UsuarioRepository;
import br.com.zup.ZupNotion.usuario.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.constraints.AssertTrue;

@SpringBootTest
public class UsuarioServiceTest {
    @MockBean
    private UsuarioRepository usuarioRepository;

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
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class)));
        Mockito.when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);

    }

    /*@Test
    public void testandoValidacaoDeEmailZup(){
        var validacaoDeEmail =
        usuarioService.validarEmailZup("raline@zup.com.br");
        Assertions.assertTrue(validacaoDeEmail);
    }

     */
}
