package br.com.zup.ZupNotion.models.dtos;

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

}
