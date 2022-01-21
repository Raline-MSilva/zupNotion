package br.com.zup.ZupNotion.usuario.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsuarioEntradaDTO {

    @NotBlank(message = "O campo não pode estar em branco")
    private String nome;
    @NotBlank(message = "O campo não pode estar em branco")
    private String email;
    @NotBlank(message = "O campo não pode estar em branco")
    private String senha;
}
