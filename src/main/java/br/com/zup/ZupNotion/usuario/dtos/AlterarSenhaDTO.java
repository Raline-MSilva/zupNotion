package br.com.zup.ZupNotion.usuario.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AlterarSenhaDTO {
    private String email;
    @NotBlank(message = "{validacao.not-blank}")
    private String senha;

}
