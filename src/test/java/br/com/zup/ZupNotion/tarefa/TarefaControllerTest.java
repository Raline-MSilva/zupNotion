package br.com.zup.ZupNotion.tarefa;

import br.com.zup.ZupNotion.components.Conversor;
import br.com.zup.ZupNotion.components.TarefaImportacaoCSV;
import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import br.com.zup.ZupNotion.config.security.JWT.UsuarioLoginService;
import br.com.zup.ZupNotion.controllers.TarefaController;
import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.AlterarDadosTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.AlterarStatusTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.RespostaTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.TarefaResumoDTO;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.services.TarefaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@WebMvcTest({TarefaController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})

public class TarefaControllerTest {
    @MockBean
    private TarefaService tarefaService;

    @MockBean
    private UsuarioLoginService usuarioLoginService;

    @MockBean
    private JWTComponent jwtComponent;

    @MockBean
    private TarefaResumoDTO tarefaResumoDTO;

    @MockBean
    private TarefaImportacaoCSV tarefaImportacaoCSV;

    @MockBean
    private UsuarioService usuarioService;



    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Tarefa tarefa;
    private CadastroTarefaDTO cadastroTarefaDTO;
    private RespostaTarefaDTO respostaTarefaDTO;
    private Usuario usuario;
    private AlterarStatusTarefaDTO alterarStatusTarefaDTO;
    private AlterarDadosTarefaDTO alterarDadosTarefaDTO;


    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        tarefa = new Tarefa();
        tarefa.setId(1);
        tarefa.setTitulo("descansar");
        tarefa.setDescricao("pra ontem");
        tarefa.setPrioridade(Prioridade.ALTA);
        tarefa.setStatus(Status.EM_ANDAMENTO);

        cadastroTarefaDTO = new CadastroTarefaDTO();
        cadastroTarefaDTO.setTitulo("dormir");
        cadastroTarefaDTO.setDescricao("pra ontem");
        cadastroTarefaDTO.setPrioridade(Prioridade.ALTA);
        cadastroTarefaDTO.setEmail("fulano@zup.com.br");
        cadastroTarefaDTO.setEstimativaEmHoras(1);

        respostaTarefaDTO = new RespostaTarefaDTO();
        respostaTarefaDTO.setId(1);

        respostaTarefaDTO = new RespostaTarefaDTO();
        respostaTarefaDTO.setId(1);

        usuario = new Usuario();
        usuario.setId("40288f867e98c3ab017e98c3f5480000");

        tarefa.setUsuario(usuario);

        alterarStatusTarefaDTO = new AlterarStatusTarefaDTO();
        alterarStatusTarefaDTO.setStatus(Status.EM_ANDAMENTO);

        alterarDadosTarefaDTO = new AlterarDadosTarefaDTO();
        alterarDadosTarefaDTO.setTitulo("novo titulo");
        alterarDadosTarefaDTO.setDescricao("nova descricao");

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefa() throws Exception {
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.status().is(201)));

        String jsonResposta = resultado.andReturn().getResponse().getContentAsString();
        RespostaTarefaDTO respostaTarefaDTO = objectMapper.readValue(jsonResposta, RespostaTarefaDTO.class);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoTituloEmBranco() throws Exception {
        cadastroTarefaDTO.setTitulo("");
        Mockito.when((tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString()))).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoPrioridadeNulo() throws Exception {
        cadastroTarefaDTO.setPrioridade(null);
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(422));
    }

    private ResultActions realizarRequisicao(Object object, int statusEsperado, String httpVerbo, String complemento) throws Exception {
        String json = objectMapper.writeValueAsString(object);
        URI uri = UriComponentsBuilder.fromPath("/tarefas" + "").build().toUri();

        return mockMvc.perform(MockMvcRequestBuilders.request(httpVerbo, uri)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(statusEsperado));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testarAtualizarStatus() throws Exception {
        alterarStatusTarefaDTO.setStatus(Status.CONCLUIDA);
        Mockito.doNothing().when(tarefaService).alterarStatusTarefa(Mockito.anyInt(), Mockito.any(Status.class));
        String json = objectMapper.writeValueAsString(alterarStatusTarefaDTO);

        Assertions.assertNotEquals(alterarStatusTarefaDTO.getStatus(), tarefa.getStatus());
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/alterarStatus" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testarAtualizarStatusCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarStatusTarefa(Mockito.anyInt(), Mockito.any(Status.class));

        String json = objectMapper.writeValueAsString(alterarStatusTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/alterarStatus" + "/1")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarTituloDaTarefa() throws Exception {
        alterarDadosTarefaDTO.setTitulo("tea");

        Mockito.doNothing().when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);

        Assertions.assertNotEquals(alterarDadosTarefaDTO.getTitulo(), tarefa.getTitulo());
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarTituloCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarDescricaoDaTarefa() throws Exception {
        alterarDadosTarefaDTO.setDescricao("tia");

        Mockito.doNothing().when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);

        Assertions.assertNotEquals(alterarDadosTarefaDTO.getDescricao(), tarefa.getDescricao());
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarDescricaoCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarTarefa() throws Exception {
        Mockito.doNothing().when(tarefaService).deletarTarefa(Mockito.anyInt());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarTarefaNaoExistente() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).deletarTarefa(Mockito.anyInt());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));

    }

}
