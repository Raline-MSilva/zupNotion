package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.SenhaInvalidaException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SenhaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Usuario localizarPorEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        throw new UsuarioNaoExisteException("Usuário não existe");
    }

    public Usuario alterarSenha(Usuario usuario) {
        Usuario usuarioBanco = localizarPorEmail(usuario.getEmail());
        usuarioBanco.setSenha(usuario.getSenha());
        criptografarSenha(usuarioBanco);
        return usuarioRepository.save(usuarioBanco);
    }

    public void criptografarSenha(Usuario usuario){
        String senhaEscondida = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaEscondida);
    }

}
