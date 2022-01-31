package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import lombok.Data;

@Data
public class TarefaResumoDTO {
    private Integer id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private Status status;
    private int estimativaEmHoras;

    public TarefaResumoDTO(Tarefa obj) {
        this.id = obj.getId();
        this.titulo = obj.getTitulo();
        this.descricao = obj.getDescricao();
        this.prioridade = obj.getPrioridade();
        this.status = obj.getStatus();
        this.estimativaEmHoras = obj.getEstimativaEmHoras();
    }

}
