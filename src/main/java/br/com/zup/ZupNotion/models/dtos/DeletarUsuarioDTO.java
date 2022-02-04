package br.com.zup.ZupNotion.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeletarUsuarioDTO {

    @NotBlank(message = "{validacao.not-blank}")
    private String email;
}
