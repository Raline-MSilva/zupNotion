package br.com.zup.ZupNotion.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AlterarSenhaDTO {

    @NotBlank(message = "{validacao.not-blank}")
    private String email;
    @NotBlank(message = "{validacao.not-blank}")
    private String perguntaDeSeguranca;
    @NotBlank(message = "{validacao.not-blank}")
    private String respostaDeSeguranca;
    @NotBlank(message = "{validacao.not-blank}")
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8}$",
            message="{validacao.senha-forte}")
    private String senha;

}
