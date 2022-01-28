package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class TarefaServiceTest {

    @MockBean
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaService tarefaService;

    private Tarefa tarefa;

    @BeforeEach
    public void setup(){
        tarefa = new Tarefa();
        tarefa.setTitulo("Testar");
        tarefa.setDescricao("Testar deletar");
        tarefa.setPrioridade(Prioridade.ALTA);
    }

    @Test
    public void testarDeletarTarefaSucesso(){
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        tarefaService.deletarTarefa(Mockito.anyInt());

        Mockito.verify(tarefaRepository, Mockito.times(1)).deleteById(Mockito.anyInt());

    }

    @Test
    public void testarDeletarTarefaCaminhoNegativo(){
        Mockito.when(tarefaRepository.existsById(Mockito.anyInt())).thenReturn(false);
        Mockito.doNothing().when(tarefaRepository).deleteById(Mockito.anyInt());

        TarefaNaoExisteException exception = Assertions.assertThrows(TarefaNaoExisteException.class, () -> {
            tarefaService.deletarTarefa(1);
        });
    }

    @Test
    public void testarBuscarTarefa(){
        Mockito.when(tarefaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(tarefa));
        Tarefa tarefaResposta = tarefaService.localizarTarefaPorId(Mockito.anyInt());

        Assertions.assertNotNull(tarefaResposta);
        Assertions.assertEquals(Tarefa.class,tarefaResposta.getClass());
        Assertions.assertEquals(tarefa.getId(),tarefaResposta.getId());

        Mockito.verify(tarefaRepository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    public void testarBuscarTarefaNaoEncontrado(){
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
