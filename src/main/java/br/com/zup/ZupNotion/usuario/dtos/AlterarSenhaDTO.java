package br.com.zup.ZupNotion.usuario.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AlterarSenhaDTO {
    private String email;
    @NotBlank(message = "O campo não pode estar em branco")
    private String senha;

}
