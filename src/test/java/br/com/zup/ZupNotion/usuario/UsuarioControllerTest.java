package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import br.com.zup.ZupNotion.config.security.JWT.UsuarioLoginService;
import br.com.zup.ZupNotion.controllers.UsuarioController;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.CadastroRetornoDTO;
import br.com.zup.ZupNotion.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({UsuarioController.class, UsuarioLoginService.class, JWTComponent.class})
public class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioLoginService usuarioLoginService;

    @MockBean
    private JWTComponent jwtComponent;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Usuario usuario;
    private CadastroRetornoDTO cadastroRetornoDTO;

    @BeforeEach
    public void setup() {
        usuario = new Usuario();
        usuario.setNome("Xablau");
        usuario.setEmail("xablau@zup.com.br");

        cadastroRetornoDTO = new CadastroRetornoDTO();
        cadastroRetornoDTO.setNome("Xablau2");
        cadastroRetornoDTO.setEmail("xablau2@zup.com");

        objectMapper = new ObjectMapper();
    }


    @Test
    @WithMockUser("user@user.com")
    public void testarCadastroUsuario() throws Exception {
        Mockito.when(usuarioService.cadastrarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = objectMapper.writeValueAsString(cadastroRetornoDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));

        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
        var usuarioResposta = objectMapper.readValue(jsonResposta, CadastroRetornoDTO.class);
    }
}