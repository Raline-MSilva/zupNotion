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
    private SegurancaService segurancaService;
    @MockBean
    private EmailService emailService;

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
        Usuario usuarioResposta = emailService.localizarPorEmail(usuario.getEmail());

        Assertions.assertNotNull(usuarioResposta);
        Assertions.assertEquals(Usuario.class,usuarioResposta.getClass());
        Assertions.assertEquals(usuario.getId(),usuarioResposta.getId());

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }

    @Test
    public void testarAlterarSenha() {
        String senhaCriptografada = "vgcsdhjd";
        Mockito.when(emailService.localizarPorEmail(usuario.getEmail())).thenReturn(usuario);
        Mockito.when(segurancaService.criptografarSenha(usuario.getSenha())).thenReturn(senhaCriptografada);
        Usuario usuarioCadastrado = usuario;
        usuarioCadastrado.setSenha(senhaCriptografada);
        Mockito.when(usuarioRepository.save(usuarioCadastrado)).thenReturn(usuarioCadastrado);
        Usuario usuarioRetornado = senhaService.alterarSenha(usuario);
        Assertions.assertEquals(usuarioRetornado, usuarioCadastrado);
    }
}
