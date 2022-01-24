package br.com.zup.ZupNotion.usuario.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CadastroUsuarioDTO {

    @NotBlank(message = "{validacao.not-blank}")
    @Size(min = 2, message = "{validacao.valor-minimo}")
    private String nome;
    @NotBlank(message = "{validacao.not-blank}")
    private String email;
    @NotBlank(message = "{validacao.not-blank}")
    private String senha;
}
