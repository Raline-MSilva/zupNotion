package br.com.zup.ZupNotion.tarefa;

import br.com.zup.ZupNotion.components.Conversor;
import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import br.com.zup.ZupNotion.config.security.JWT.UsuarioLoginService;
import br.com.zup.ZupNotion.controllers.TarefaController;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.CadastroTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.RespostaTarefaDTO;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.services.TarefaService;
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

@WebMvcTest({TarefaController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})

public class TarefaControllerTest {
    @MockBean
    TarefaService tarefaService;

    @MockBean
    private UsuarioLoginService usuarioLoginService;

    @MockBean
    private JWTComponent jwtComponent;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Tarefa tarefa;
    private CadastroTarefaDTO cadastroTarefaDTO;
    private RespostaTarefaDTO respostaTarefaDTO;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        tarefa = new Tarefa();
        tarefa.setTitulo("descansar");
        tarefa.setDescricao("pra ontem");
        tarefa.setPrioridade(Prioridade.ALTA);

        cadastroTarefaDTO = new CadastroTarefaDTO();
        cadastroTarefaDTO.setTitulo("dormir");
        cadastroTarefaDTO.setDescricao("pra ontem");
        cadastroTarefaDTO.setPrioridade(Prioridade.ALTA);

        respostaTarefaDTO = new RespostaTarefaDTO();
        respostaTarefaDTO.setId(1);

    }

    @Test
    @WithMockUser("tarefa@tarefa.com")
    public void testarCadastrarTarefa() throws Exception {
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(Tarefa.class))).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.status().is(201)));

        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
        RespostaTarefaDTO respostaTarefaDTO = objectMapper.readValue(jsonResposta, RespostaTarefaDTO.class);
    }

    @Test
    @WithMockUser("tarefa@tarefa.com")
    public void testarCadastrarTarefaValidacaoTituloEmBranco() throws Exception {
        cadastroTarefaDTO.setTitulo("");
        Mockito.when((tarefaService.cadastrarTarefa(Mockito.any(Tarefa.class)))).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

}
