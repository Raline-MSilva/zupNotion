package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (usuario.isPresent()) {
            tarefa.setDataDeCadastro(LocalDateTime.now());
            tarefa.setStatus(Status.A_FAZER);
            usuario.get().getTarefas().add(tarefa);
            tarefa.setUsuario(usuario.get());
            return salvarTarefa(tarefa);
        }
        throw new UsuarioNaoExisteException("Usuario não existe");
    }

    public Tarefa salvarTarefa(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> buscarTarefas() {
        Iterable<Tarefa> listaTarefas = tarefaRepository.findAll();
        return (List<Tarefa>) listaTarefas;
    }

    public Tarefa localizarTarefaPorId(Integer id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            return tarefaOptional.get();
        }
        throw new TarefaNaoExisteException("Tarefa não existe");
    }

    public void deletarTarefa(Integer id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            tarefaRepository.deleteById(id);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void alterarTarefaPorId(Tarefa tarefa, Integer id){
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            tarefa.setTitulo(tarefa.getTitulo());
            tarefa.setDescricao(tarefa.getDescricao());
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

}
