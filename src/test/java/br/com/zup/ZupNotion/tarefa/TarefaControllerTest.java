package br.com.zup.ZupNotion.tarefa;

import br.com.zup.ZupNotion.components.Conversor;
import br.com.zup.ZupNotion.components.TarefaImportacaoCSV;
import br.com.zup.ZupNotion.config.ResponseMessage;
import br.com.zup.ZupNotion.config.security.JWT.JWTComponent;
import br.com.zup.ZupNotion.config.security.JWT.UsuarioLoginService;
import br.com.zup.ZupNotion.controllers.TarefaController;
import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.AlterarDadosTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.AlterarStatusTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.InformarEmailDTO;
import br.com.zup.ZupNotion.models.dtos.RespostaTarefaDTO;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.services.TarefaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TarefaController.class, Conversor.class, UsuarioLoginService.class, JWTComponent.class})

public class TarefaControllerTest {
    @MockBean
    private TarefaService tarefaService;

    @MockBean
    private UsuarioLoginService usuarioLoginService;

    @MockBean
    private JWTComponent jwtComponent;

    @MockBean
    private TarefaImportacaoCSV tarefaImportacaoCSV;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private Tarefa tarefa;
    private CadastroTarefaDTO cadastroTarefaDTO;
    private RespostaTarefaDTO respostaTarefaDTO;
    private Usuario usuario;
    private AlterarStatusTarefaDTO alterarStatusTarefaDTO;
    private AlterarDadosTarefaDTO alterarDadosTarefaDTO;
    private Page<Tarefa> pageTarefa;
    private Pageable pageable;
    private MultipartFile file;
    private InformarEmailDTO informarEmailDTO;

    public static final String DESCRICAO_SIZE = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. " +
            "Facilis enim eum ad nam temporibus et unde quidem quae quis, velit consectetur fugit nobis ducimus ipsum "
            + "molestiae praesentium corporis magnam? Necessitatibus.";


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

        informarEmailDTO = new InformarEmailDTO();
        informarEmailDTO.setEmail("taline@zup.com.br");

        List<Tarefa> tarefas = new ArrayList<>();
        tarefas.add(tarefa);

        pageable = PageRequest.of(0, tarefas.size());

        pageTarefa = new PageImpl(tarefas, pageable, tarefas.size());


    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefa() throws Exception {
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().is(201)));

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
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoPrioridadeNulo() throws Exception {
        cadastroTarefaDTO.setPrioridade(null);
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoTamanhoMaximoDaDescricao() throws Exception {
        cadastroTarefaDTO.setDescricao(DESCRICAO_SIZE);
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoTamanhoMinimoDaDescricao() throws Exception {
        cadastroTarefaDTO.setDescricao("te");
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoTamanhoMaximoDoTitulo() throws Exception {
        cadastroTarefaDTO.setTitulo(DESCRICAO_SIZE);
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarCadastrarTarefaValidacaoTamanhoMinimoDoTitulo() throws Exception {
        cadastroTarefaDTO.setTitulo("ti");
        Mockito.when(tarefaService.cadastrarTarefa(Mockito.any(), Mockito.anyString())).thenReturn(tarefa);
        String json = objectMapper.writeValueAsString(cadastroTarefaDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    private ResultActions realizarRequisicao(Object object, int statusEsperado, String httpVerbo, String complemento) throws Exception {
        String json = objectMapper.writeValueAsString(object);
        URI uri = UriComponentsBuilder.fromPath("/tarefas" + "").build().toUri();

        return mockMvc.perform(MockMvcRequestBuilders.request(httpVerbo, uri)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(statusEsperado));
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
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    public void testarAtualizarStatusCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarStatusTarefa(Mockito.anyInt(), Mockito.any(Status.class));

        String json = objectMapper.writeValueAsString(alterarStatusTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/alterarStatus" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
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
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarTituloCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
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
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtualizarDescricaoCaminhoNegativo() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).alterarDadosTarefa(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());

        String json = objectMapper.writeValueAsString(alterarDadosTarefaDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarTarefa() throws Exception {
        Mockito.doNothing().when(tarefaService).deletarTarefa(Mockito.anyInt());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarDeletarTarefaNaoExistente() throws Exception {
        Mockito.doThrow(TarefaNaoExisteException.class).when(tarefaService).deletarTarefa(Mockito.anyInt());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.delete("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarBuscarTarefas() throws Exception {
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.when(tarefaService.buscarTarefas(Mockito.anyString(), Mockito.anyString(), pageableCaptor.capture())).thenReturn(pageTarefa);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/tarefas")
                        .param("size", "2")
                        .param("page", "0").header("Authorization", "xablau")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));


        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageable.getPageSize(), 2);
        Assertions.assertEquals(pageable.getPageNumber(), 0);
        Mockito.verify(tarefaService).buscarTarefas(Mockito.anyString(), Mockito.anyString(),
                pageableCaptor.capture());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testarBuscarTarefasPorStatus() throws Exception {
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/tarefas")
                        .param("status", "CONCLUIDA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(tarefaService).buscarTarefas(Mockito.anyString(), Mockito.anyString(),
                pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageable.getPageSize(), 2);
        Assertions.assertEquals(pageable.getPageNumber(), 0);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void testarBuscarTarefasPorPrioridade() throws Exception {
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.get("/tarefas")
                        .param("prioridade", "ALTA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(tarefaService).buscarTarefas(Mockito.anyString(), Mockito.anyString(),
                pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageable.getPageSize(), 2);
        Assertions.assertEquals(pageable.getPageNumber(), 0);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAnexarArquivoCSV() throws Exception {
        MockMultipartFile file = new MockMultipartFile("arquivo", "arquivo.csv", "text/csv",
                "arquivo.csv".getBytes());
        String message = "Upload realizado com sucesso: " + file.getOriginalFilename();

        Mockito.when(tarefaService.validarSeArquivoCSV(Mockito.any())).thenReturn(true);
        Mockito.doNothing().when(tarefaService).salvarCSV(Mockito.any());

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.multipart("/tarefas/arquivosCSV")
                        .file("file", "arquivo.csv".getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(status().is(200));

        Assertions.assertEquals("Upload realizado com sucesso: arquivo.csv",
                new ResponseMessage(message).getMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarArquivoQueNaoECSV() throws Exception {
        MockMultipartFile file = new MockMultipartFile("arquivo", "arquivo.txt", "text/txt",
                "arquivo.txt".getBytes());

        Mockito.when(tarefaService.validarSeArquivoCSV(Mockito.any())).thenReturn(false);
        Mockito.doNothing().when(tarefaService).salvarCSV(Mockito.any());


        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/tarefas/arquivosCSV").file("file", "arquivo.txt".getBytes()))
                .andExpect(status().is(417));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtribuirTarefaPorIdCaminhoPositivo() throws Exception{
        informarEmailDTO.setEmail("maria@zup.com.br");
        Mockito.doNothing().when(tarefaService).atribuirTarefa(Mockito.anyInt(), Mockito.anyString());
        String json = objectMapper.writeValueAsString(informarEmailDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/atribuirTarefa" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtribuirTarefaPorIdValidacaoEmailEmBranco() throws Exception{
        informarEmailDTO.setEmail("");
        Mockito.doNothing().when(tarefaService).atribuirTarefa(Mockito.anyInt(), Mockito.anyString());
        String json = objectMapper.writeValueAsString(informarEmailDTO);

        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/atribuirTarefa" + "/1")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(422));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testarAtribuirTarefaPorIdCaminhoNegativo() throws Exception {
        informarEmailDTO.setEmail("taline@gmail.com.br");
        Mockito.doThrow(UsuarioNaoExisteException.class).when(tarefaService).atribuirTarefa(Mockito.anyInt(), Mockito.anyString());

        String json = objectMapper.writeValueAsString(informarEmailDTO);
        ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.patch("/tarefas" + "/atribuirTarefa" + "/1")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));

    }

}
