package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Role;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TarefaServiceTest {

    @MockBean
    private TarefaRepository tarefaRepository;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private TarefaService tarefaService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UsuarioRepository usuarioRepository;


    private Tarefa tarefa;
    private Usuario usuario;
    private Usuario usuario2;
    private Pageable pageable;
    private Page<Tarefa> pageTarefas;

    @BeforeEach
    public void setup() {
        tarefa = new Tarefa();
        tarefa.setId(1);
        tarefa.setTitulo("Testar");
        tarefa.setDescricao("Testar deletar");
        tarefa.setPrioridade(Prioridade.ALTA);
        tarefa.setStatus(Status.CONCLUIDA);

        usuario = new Usuario();
        usuario.setNome("ana");
        tarefa.setUsuario(usuario);
        usuario.setEmail("ar@zup.com.br");

        List<Tarefa> tarefas = new ArrayList<>();
        usuario.setTarefas(tarefas);
        usuario.getTarefas().add(tarefa);

        usuario2 = new Usuario();
        usuario2.setNome("lia");

        pageable = PageRequest.of(0, tarefas.size());
        pageTarefas = new PageImpl(tarefas, pageable, tarefas.size());

    }

    @Test
    public void testarCadastrarTarefa() {
        Mockito.when(emailService.localizarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        tarefaService.cadastrarTarefa(tarefa, usuario.getEmail());
        Assertions.assertEquals(tarefa.getStatus(), Status.A_FAZER);
        Assertions.assertEquals(tarefa.getDataDeCadastro().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        Assertions.assertEquals(tarefa.getUsuario(), usuario);
        Assertions.assertEquals(usuario.getTarefas(), tarefa.getUsuario().getTarefas());

    }

    @Test
    public void testarAlterarDadosDaTarefaCaminhoPositivo() {
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.when(tarefaService.salvarTarefa(Mockito.any())).thenReturn(tarefa);
        testarLocalizarTarefaPorId();
        tarefa.setTitulo("ler");
        tarefa.setDescricao("30 min por dia");

        tarefaService.alterarDadosTarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao());
        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(tarefa.getId());

    }

    @Test
    public void testarAlterarDdosDaTarefaCaminhoNegativo() {
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(tarefaService.salvarTarefa(Mockito.any())).thenReturn(tarefa);

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.alterarDadosTarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao());
        });

        Assertions.assertEquals("Tarefa não existe", exception.getMessage());
        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(tarefa.getId());
    }

    @Test
    public void testarAlterarStatusDaTarefaCaminhoPositivo() {
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        testarLocalizarTarefaPorId();

        tarefa.setStatus(Status.EM_ANDAMENTO);
        tarefaService.alterarStatusTarefa(tarefa.getId(), tarefa.getStatus());
        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(tarefa.getId());

    }

    @Test
    public void testarAlterarStatusDaTarefaCaminhoNegativo() {
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.alterarDadosTarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao());
        });

        Assertions.assertEquals("Tarefa não existe", exception.getMessage());
        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(tarefa.getId());

    }

    @Test
    public void testarDeletarTarefaSucesso() {
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        tarefaService.deletarTarefa(tarefa.getId());
        Mockito.verify(tarefaRepository, Mockito.times(1)).deleteById(Mockito.anyInt());

    }

    @Test
    public void testarDeletarTarefaCaminhoNegativo() {
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario2);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.deletarTarefa(1);
        });
    }

    @Test
    public void testarLocalizarTarefaPorId() {
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Tarefa tarefaResposta = tarefaService.localizarTarefaPorId(Mockito.anyInt());

        Assertions.assertNotNull(tarefaResposta);
        Assertions.assertEquals(Tarefa.class, tarefaResposta.getClass());
        Assertions.assertEquals(tarefa.getId(), tarefaResposta.getId());

        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void testarLocalizarTarefaPorIdNaoEncontrado() {
        Mockito.when(tarefaRepository.save(Mockito.any())).thenReturn(tarefa);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.localizarTarefaPorId(0);
        });

        Assertions.assertEquals("Tarefa não existe", exception.getMessage());

    }

    @Test
    public void testarSalvarTarefa() {
        Mockito.when(tarefaRepository.save(tarefa))
                .thenAnswer(objto -> objto.getArgument(0, Tarefa.class));

        tarefaService.salvarTarefa(tarefa);

        Mockito.verify(tarefaRepository, Mockito.times(1)).save(tarefa);

    }

    @Test
    public void testarBuscarTodasAsTarefas() {
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        usuario.setRole(Role.ROLE_ADMIN);
        Mockito.when(tarefaRepository.findAll(pageable)).thenReturn(pageTarefas);
        tarefaService.buscarTarefas(String.valueOf(tarefa.getStatus()), String.valueOf(tarefa.getPrioridade()), pageable);


        Mockito.verify(tarefaRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(tarefaRepository, Mockito.times(0)).findAllByStatus(tarefa.getStatus(), pageable);
        Mockito.verify(tarefaRepository, Mockito.times(0)).findAllByPrioridade(tarefa.getPrioridade(), pageable);

    }

    @Test
    public void testarBuscarTarefasPorStatus() {
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        usuario.setRole(Role.ROLE_USER);
        tarefa.setStatus(Status.A_FAZER);
        Mockito.when(tarefaRepository.findAllByStatus(tarefa.getStatus(), pageable)).thenReturn(pageTarefas);
        tarefaService.buscarTarefas(String.valueOf(tarefa.getStatus()), String.valueOf(tarefa.getPrioridade()), pageable);

        Mockito.verify(tarefaRepository, Mockito.times(0)).findAll(pageable);
        Mockito.verify(tarefaRepository, Mockito.times(1)).findAllByStatus(tarefa.getStatus(), pageable);
        Mockito.verify(tarefaRepository, Mockito.times(0)).findAllByPrioridade(tarefa.getPrioridade(), pageable);
    }

    @Test
    public void testarBuscarTarefasPorPrioridade() {
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        usuario.setRole(Role.ROLE_USER);
        String tarefaNull = null;
        tarefa.setPrioridade(Prioridade.BAIXA);
        Mockito.when(tarefaRepository.findAllByPrioridade(tarefa.getPrioridade(), pageable)).thenReturn(pageTarefas);
        tarefaService.buscarTarefas(tarefaNull, String.valueOf(tarefa.getPrioridade()), pageable);

        Mockito.verify(tarefaRepository, Mockito.times(0)).findAll(pageable);
        Mockito.verify(tarefaRepository, Mockito.times(0)).findAllByStatus(tarefa.getStatus(), pageable);
        Mockito.verify(tarefaRepository, Mockito.times(1)).findAllByPrioridade(tarefa.getPrioridade(), pageable);

    }

    @Test
    public void testarAtribuirTarefaCaminhoPositivo() {
        testarLocalizarTarefaPorId();
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        Mockito.when(emailService.localizarUsuarioPorEmail(usuario.getEmail())).thenReturn(usuario);
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);

        usuario.setEmail(usuario.getEmail());
        tarefa.setUsuario(usuario);
        tarefaService.atribuirTarefa(tarefa.getId(), usuario.getEmail());

    }

}
