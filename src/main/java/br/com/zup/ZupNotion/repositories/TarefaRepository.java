package br.com.zup.ZupNotion.repositories;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.enums.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TarefaRepository extends CrudRepository<Tarefa, Integer> {

    List<Tarefa> findAllByStatus(Status status);

}
