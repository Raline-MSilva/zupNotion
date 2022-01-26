package br.com.zup.ZupNotion.repositories;

import br.com.zup.ZupNotion.models.Tarefa;
import org.springframework.data.repository.CrudRepository;

public interface TarefaRepository extends CrudRepository<Tarefa, String> {

}
