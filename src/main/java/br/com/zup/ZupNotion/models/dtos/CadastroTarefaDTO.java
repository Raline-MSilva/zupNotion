package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CadastroTarefaDTO {

    @Size(min = 3, max = 50, message = "{validacao.titulo-size}")
    @NotBlank
    private String titulo;
    @Size(min = 3, max = 100, message = "{validacao.descricao-size}")
    private String descricao;
    @NotNull
    private Prioridade prioridade;
    private int estimativaEmHoras;
    @NotBlank(message = "{validacao.not-blank}")
    private String email;

}
