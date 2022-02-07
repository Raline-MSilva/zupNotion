package br.com.zup.ZupNotion.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AlterarDadosTarefaDTO {
    @Size(min = 3, max = 50, message = "{validacao.titulo-size}")
    @NotBlank
    private String titulo;
    @Size(min = 3, max = 100, message = "{validacao.descricao-size}")
    private String descricao;

}
