package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void cadastrarTarefa(Tarefa tarefa){
        String id = tarefa.getUsuario().getId();
        if (usuarioRepository.existsById(id)){
            tarefa.setDataDeCadastro(LocalDateTime.now());
            tarefa.setStatus(Status.A_FAZER);
            tarefaRepository.save(tarefa);
        }
        throw new UsuarioNaoExisteException("Usuario não existe");
    }

}
