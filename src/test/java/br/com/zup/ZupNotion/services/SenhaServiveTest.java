package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class SenhaServiveTest {

    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SenhaService senhaService;

    private Usuario usuario;

    @BeforeEach
    public void setup(){
        usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setEmail("Maria@zup.com.br");
        usuario.setSenha("123mariA@");
    }

    @Test
    public void testarLocalizarPorEmail(){
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
        Usuario usuarioResposta = senhaService.localizarPorEmail(usuario.getEmail());

        Assertions.assertNotNull(usuarioResposta);
        Assertions.assertEquals(Usuario.class,usuarioResposta.getClass());
        Assertions.assertEquals(usuario.getId(),usuarioResposta.getId());

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }


}
