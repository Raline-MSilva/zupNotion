package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Data
public class CadastroTarefaDTO {

    @Max(value = 50, message = "{validacao.titulo-max}")
    private String titulo;
    @Max(value = 100, message = "{validacao.descricao-max}")
    private String descricao;
    private Prioridade prioridade;
    private Duration estimativaEmHoras;
    @NotBlank
    private Usuario usuario;

}
