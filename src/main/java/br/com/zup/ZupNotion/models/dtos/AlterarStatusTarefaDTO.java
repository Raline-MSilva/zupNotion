package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.enums.Status;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlterarStatusTarefaDTO {

    @NotNull
    private Status status;

}
