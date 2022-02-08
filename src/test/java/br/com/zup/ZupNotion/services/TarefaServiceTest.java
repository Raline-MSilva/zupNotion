package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    private Tarefa tarefa;
    private Usuario usuario;
    private Usuario usuario2;

    @BeforeEach
    public void setup(){
        tarefa = new Tarefa();
        tarefa.setId(1);
        tarefa.setTitulo("Testar");
        tarefa.setDescricao("Testar deletar");
        tarefa.setPrioridade(Prioridade.ALTA);

        usuario = new Usuario();
        usuario.setNome("ana");
        tarefa.setUsuario(usuario);

        List<Tarefa> tarefas = new ArrayList<>();
        usuario.setTarefas(tarefas);
        usuario.getTarefas().add(tarefa);

        usuario2 = new Usuario();
        usuario2.setNome("lia");
    }

    @Test
    public void testarDeletarTarefaSucesso(){
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        tarefaService.deletarTarefa(tarefa.getId());
        Mockito.verify(tarefaRepository, Mockito.times(1)).deleteById(Mockito.anyInt());

    }

    @Test
    public void testarDeletarTarefaCaminhoNegativo(){
        Mockito.when(usuarioService.buscarUsuarioLogado()).thenReturn(usuario2);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.deletarTarefa(1);
        });
    }

    @Test
    public void testarLocalizarTarefaPorId(){
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Tarefa tarefaResposta = tarefaService.localizarTarefaPorId(Mockito.anyInt());

        Assertions.assertNotNull(tarefaResposta);
        Assertions.assertEquals(Tarefa.class,tarefaResposta.getClass());
        Assertions.assertEquals(tarefa.getId(),tarefaResposta.getId());

        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void testarLocalizarTarefaPorIdNaoEncontrado(){
        Mockito.when(tarefaRepository.save(Mockito.any())).thenReturn(tarefa);
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () ->{
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

}
