package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.ErroAoLerArquivoException;
import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.TarefaImportacaoCSV;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private TarefaImportacaoCSV importacaoCSV;

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

    public Page<Tarefa> buscarTarefas(String id, String status, String prioridade, Pageable pageable) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (status != null) {
            List<Tarefa> tarefasPorStatus = new ArrayList<>();

            Status statusTarefa = Status.valueOf(status);
            for (Tarefa tarefa : tarefaRepository.findAllByStatus(statusTarefa, pageable)) {

                if (tarefa.getUsuario() == usuario.get()) {
                    tarefasPorStatus.add(tarefa);
                }
            }
            return new PageImpl<>(tarefasPorStatus, pageable, tarefasPorStatus.size());
        }
        if (prioridade != null) {
            List<Tarefa> tarefasPorPrioridade = new ArrayList<>();

            Prioridade prioridadeTarefa = Prioridade.valueOf(prioridade);
            for (Tarefa tarefa : tarefaRepository.findAllByPrioridade(prioridadeTarefa, pageable)) {
                if (tarefa.getUsuario() == usuario.get()) {
                    tarefasPorPrioridade.add(tarefa);
                }
            }
            return new PageImpl<>(tarefasPorPrioridade, pageable, tarefasPorPrioridade.size());
        }

        return tarefaRepository.findAllByUsuario(usuario.get(), pageable);
    }

    public Tarefa localizarTarefaPorId(Integer id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            return (Tarefa) tarefaOptional.get();
        }
        throw new TarefaNaoExisteException("Tarefa não existe");
    }

    public void alterarStatusTarefa(Integer tarefaId, String usuarioId, Status status) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);

        if (tarefa.getUsuario() == buscarUsuario(usuarioId)) {
            tarefa.setStatus(status);
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void alterarDadosTarefa(Integer tarefaId, String usuarioId, String titulo, String descricao) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);

        if (tarefa.getUsuario() == buscarUsuario(usuarioId)) {
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public Usuario buscarUsuario(String usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return usuario.get();
        }
        throw new UsuarioNaoExisteException("Usuário não existe");
    }

    public void deletarTarefa(Integer tarefaId, String usuarioId) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);
        Usuario usuario = buscarUsuario(usuarioId);
        if (tarefa.getUsuario() == usuario) {
            usuario.getTarefas().remove(tarefa);
            tarefaRepository.deleteById(tarefaId);
        }else{
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void salvarCSV(MultipartFile file, String usuarioId) {
        try {
            List<Tarefa> listaTarefas = importacaoCSV.salvarTarefasComUsuario(usuarioId, file.getInputStream());
            tarefaRepository.saveAll(listaTarefas);
        } catch (IOException e) {
            throw new ErroAoLerArquivoException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public boolean validarSeArquivoCSV(MultipartFile file) {
        if (importacaoCSV.verificarFormatoCSV(file)) {
            return true;
        }
        return false;
    }


}
