package br.com.zup.ZupNotion.models.dtos;

import br.com.zup.ZupNotion.models.enums.Role;
import lombok.Data;

@Data
public class DadosUsuarioDTO {
    private String nome;
    private String email;
    private Role role;

}
