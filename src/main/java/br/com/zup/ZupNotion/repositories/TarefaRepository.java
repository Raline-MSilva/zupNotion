package br.com.zup.ZupNotion.repositories;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TarefaRepository extends PagingAndSortingRepository<Tarefa, Integer> {

    Page<Tarefa> findAllByUsuario(Usuario usuario, Pageable pageable);
    Page<Tarefa> findAllByStatus(Status status, Pageable pageable);
    Page<Tarefa> findAllByPrioridade(Prioridade prioridade, Pageable pageable);

}
