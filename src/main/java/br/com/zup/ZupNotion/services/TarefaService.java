package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Tarefa cadastrarTarefa(Tarefa tarefa, String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        tarefa.setDataDeCadastro(LocalDateTime.now());
        tarefa.setStatus(Status.A_FAZER);
        usuario.get().getTarefas().add(tarefa);
        tarefa.setUsuario(usuario.get());
        return salvarTarefa(tarefa);
    }

    public Tarefa salvarTarefa(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> buscarTarefas(String id, Status status, Prioridade prioridade) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (status != null) {
            List<Tarefa> tarefasPorStatus = new ArrayList<>();

            for (Tarefa tarefa : tarefaRepository.findAllByStatus(status)) {

                if (tarefa.getUsuario() == usuario.get()) {
                    tarefasPorStatus.add(tarefa);
                }
            }

            return tarefasPorStatus;
        }
        if (prioridade != null) {
            List<Tarefa> tarefasPorPrioridade = new ArrayList<>();

            for (Tarefa tarefa : tarefaRepository.findAllByPrioridade(prioridade)) {
                if (tarefa.getUsuario() == usuario.get()) {
                    tarefasPorPrioridade.add(tarefa);
                }
            }

            return tarefasPorPrioridade;
        }

        return usuario.get().getTarefas();
    }

    public Tarefa localizarTarefaPorId(Integer id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            return (Tarefa) tarefaOptional.get();
        }
        throw new TarefaNaoExisteException("Tarefa não existe");
    }

    public void deletarTarefa(Integer tarefaId, String usuarioId) {
        try {
            Tarefa tarefa = localizarTarefaPorId(tarefaId);
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            usuario.get().getTarefas().remove(tarefa);
            tarefaRepository.deleteById(tarefaId);
        }
        catch (TarefaNaoExisteException exception){
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

}
