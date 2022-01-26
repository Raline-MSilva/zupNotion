package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void cadastrarTarefa(Tarefa tarefa){
        tarefaRepository.save(tarefa);
    }
}
