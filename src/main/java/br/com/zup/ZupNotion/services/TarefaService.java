package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.ErroAoLerArquivoException;
import br.com.zup.ZupNotion.exceptions.TarefaNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.components.TarefaImportacaoCSV;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TarefaImportacaoCSV importacaoCSV;
    @Autowired
    private EmailService emailService;

    public Tarefa cadastrarTarefa(Tarefa tarefa, String email) {
        Usuario usuario = emailService.localizarUsuarioPorEmail(email);
        tarefa.setDataDeCadastro(LocalDateTime.now());
        tarefa.setStatus(Status.A_FAZER);
        usuario.getTarefas().add(tarefa);
        tarefa.setUsuario(usuario);
        return salvarTarefa(tarefa);
    }

    public Tarefa salvarTarefa(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public Page<Tarefa> buscarTarefas( String status, String prioridade, Pageable pageable) {

        Usuario usuario = usuarioService.buscarUsuarioLogado();

        if (Objects.equals(usuario.getRole(), "ROLE_ADMIN")) {
            return tarefaRepository.findAll(pageable);
        } else {
            if (status != null) {
                Status statusTarefa = Status.valueOf(status);
                List<Tarefa> tarefasPorStatus = tarefaRepository.findAllByStatus(statusTarefa, pageable).stream()
                        .filter(t -> t.getUsuario() == usuario).collect(Collectors.toList());
                return new PageImpl<>(tarefasPorStatus, pageable, tarefasPorStatus.size());
            }

            if (prioridade != null) {
                Prioridade prioridadeTarefa = Prioridade.valueOf(prioridade);
                List<Tarefa> tarefasPorPrioridade = tarefaRepository.findAllByPrioridade(prioridadeTarefa, pageable).stream()
                        .filter(t -> t.getUsuario() == usuario).collect(Collectors.toList());
                return new PageImpl<>(tarefasPorPrioridade, pageable, tarefasPorPrioridade.size());
            }

            return tarefaRepository.findAllByUsuario(usuario, pageable);
        }
    }

    public Tarefa localizarTarefaPorId(Integer id) {
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            return tarefaOptional.get();
        }
        throw new TarefaNaoExisteException("Tarefa não existe");
    }

    public void alterarDadosTarefa(Integer tarefaId, String titulo, String descricao) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);

        if (tarefa != null) {
            tarefa.setTitulo(titulo);
            tarefa.setDescricao(descricao);
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void alterarStatusTarefa(Integer tarefaId, Status status) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);

        if (tarefa.getUsuario() == usuarioService.buscarUsuarioLogado()) {
            tarefa.setStatus(status);
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void deletarTarefa(Integer tarefaId) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        if (tarefa.getUsuario() == usuario) {
            usuario.getTarefas().remove(tarefa);
            tarefaRepository.deleteById(tarefaId);
        }else{
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void atribuirTarefa(Integer tarefaId, String email) {
        Tarefa tarefa = localizarTarefaPorId(tarefaId);
        Usuario usuario = emailService.localizarUsuarioPorEmail(email);

        if (tarefa.getUsuario() == usuarioService.buscarUsuarioLogado()) {
            tarefa.setUsuario(usuario);
            salvarTarefa(tarefa);
        } else {
            throw new TarefaNaoExisteException("Tarefa não existe");
        }
    }

    public void salvarCSV(MultipartFile file) {
        try {
            List<Tarefa> listaTarefas = importacaoCSV.salvarTarefasComUsuario(file.getInputStream());
            tarefaRepository.saveAll(listaTarefas);
        } catch (IOException e) {
            throw new ErroAoLerArquivoException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public boolean validarSeArquivoCSV(MultipartFile file) {
        return importacaoCSV.verificarFormatoCSV(file);
    }

}
