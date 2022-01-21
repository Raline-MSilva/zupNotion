package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.usuario.dtos.CadastroUsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;
    private ModelMapper modelMapper;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, ModelMapper){
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CadastroUsuarioDTO cadastrarUsuario (@RequestBody CadastroUsuarioDTO cadastroUsuarioDTO){
        Usuario usuario = modelMapper.map(cadastroUsuarioDTO, Usuario.class);
        return modelMapper.map(usuarioService.cadastrarUsuario(usuario), CadastroUsuarioDTO.class);
    }

}
