package br.com.zup.ZupNotion.controllers;

import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.models.dtos.DeletarUsuarioDTO;
import br.com.zup.ZupNotion.services.SenhaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import br.com.zup.ZupNotion.models.dtos.AlterarSenhaDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroUsuarioDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@Api(value = "API para gerenciamento de tarefas")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private SenhaService senhaService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ApiOperation(value = "Método responsável por cadastrar um usuário")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarUsuario(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
        usuarioService.cadastrarUsuario(usuario);
    }

    @PostMapping("/cadastraradmin")
    @ApiOperation(value = "Método responsável por cadastrar um admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarAdmin(@RequestBody @Valid CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
        usuarioService.cadastrarAdmin(usuario);
    }

    @PatchMapping("/esqueciSenha")
    @ApiOperation(value = "Método responsável por alterar a senha de um usuário")
    @ResponseStatus(HttpStatus.OK)
    public void alterarSenha(@RequestBody @Valid AlterarSenhaDTO alterarSenhaDTO) {
        senhaService.alterarSenha(modelMapper.map(alterarSenhaDTO, Usuario.class));
    }

    @DeleteMapping("/deletarUsuario")
    @ApiOperation(value = "Método responsável por deletar um usuário")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarUsuario (@RequestBody @Valid DeletarUsuarioDTO deletarUsuarioDTO){
        usuarioService.deletarUsuario(deletarUsuarioDTO.getEmail());
    }
}