package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.enums.Prioridade;
import lombok.Data;

import java.time.Duration;

@Data
public class TarefaResumoDTO {
    private Integer id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private Duration estimativaEmHoras;

}
