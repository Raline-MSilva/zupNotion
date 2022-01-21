package br.com.zup.ZupNotion.usuario.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CadastroUsuarioDTO {

    @NotBlank(message = "O campo não pode estar em branco")
    @Size(min = 2, message = "O campo deve ter pelo menos dois caracteres")
    private String nome;
    @NotBlank(message = "O campo não pode estar em branco")
    private String email;
    @NotBlank(message = "O campo não pode estar em branco")
    private String senha;
}
