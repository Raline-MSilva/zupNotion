package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class SenhaServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private EmailService emailService;

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
    public void testarAlterarSenha() {
        String senhaCriptografada = "vgcsdhjd";
        Mockito.when(emailService.localizarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        Mockito.when(senhaService.criptografarSenha(usuario.getSenha())).thenReturn(senhaCriptografada);
        Usuario usuarioCadastrado = usuario;
        usuarioCadastrado.setSenha(senhaCriptografada);
        Mockito.when(usuarioRepository.save(usuarioCadastrado)).thenReturn(usuarioCadastrado);
        Usuario usuarioRetornado = senhaService.alterarSenha(usuario);
        Assertions.assertEquals(usuarioRetornado, usuarioCadastrado);
    }


}
