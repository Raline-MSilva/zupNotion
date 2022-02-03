package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.enums.Status;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class AlterarDadosTarefaDTO {
    private Status status;
    private String titulo;
    @Size(min = 3, max = 100, message = "{validacao.descricao-size}")
    private String descricao;

}
