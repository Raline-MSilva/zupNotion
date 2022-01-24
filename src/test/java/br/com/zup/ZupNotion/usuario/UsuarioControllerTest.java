package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.services.UsuarioService;
import br.com.zup.ZupNotion.usuario.dtos.CadastroUsuarioDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UsuarioController.class)

public class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Usuario usuario;
    private CadastroUsuarioDTO cadastroUsuarioDTO;

    @BeforeEach
    public void setup(){
        usuario = new Usuario();
        usuario.setNome("Xablau");

        cadastroUsuarioDTO = new CadastroUsuarioDTO();
        cadastroUsuarioDTO.setNome("Xablau2");

        objectMapper = new ObjectMapper();
    }


    @Test
    public void testarCadastroUsuario() throws Exception {
        String json = objectMapper.writeValueAsString(cadastroUsuarioDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/usuario")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));



    }
}
