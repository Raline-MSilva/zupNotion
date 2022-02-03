package br.com.zup.ZupNotion.components;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.dtos.TarefaResumoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversorDeTarefasComPaginacao {

    @Autowired
    private ModelMapper modelMapper;

    public List<TarefaResumoDTO> converterPaginaEmLista(Page<Tarefa> tarefaService) {
        List<TarefaResumoDTO> tarefas = new ArrayList<>();
        for (Tarefa tarefa : tarefaService){
            TarefaResumoDTO tarefaResumoDTO = modelMapper.map(tarefa, TarefaResumoDTO.class);
            tarefas.add(tarefaResumoDTO);
        }
        return tarefas;
    }
}
