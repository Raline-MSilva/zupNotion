package br.com.zup.ZupNotion.repositories;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface TarefaRepository extends PagingAndSortingRepository<Tarefa, Integer> {

    List<Tarefa> findAllByStatus(Status status, Pageable pageable);
    List<Tarefa> findAllByPrioridade(Prioridade prioridade);

}
