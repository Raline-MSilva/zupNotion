package br.com.zup.ZupNotion.controllers;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.services.SenhaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import br.com.zup.ZupNotion.models.dtos.AlterarSenhaDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroUsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private SenhaService senhaService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
        usuarioService.cadastrarUsuario(usuario);
    }

    @PutMapping("/esqueciSenha")
    @ResponseStatus(HttpStatus.OK)
    public void alterarSenha(@RequestBody @Valid AlterarSenhaDTO alterarSenhaDTO) {
        senhaService.alterarSenha(modelMapper.map(alterarSenhaDTO, Usuario.class));
    }

}