package br.com.zup.ZupNotion.services;

import br.com.zup.ZupNotion.exceptions.DominioInvalidoException;
import br.com.zup.ZupNotion.exceptions.EmailJaExistenteException;
import br.com.zup.ZupNotion.exceptions.UsuarioNaoExisteException;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean verificarEmailExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaExistenteException("Email já cadastrado!");
        }
        return true;
    }

    public boolean validarEmailZup(String email) {
        Pattern padrao = Pattern.compile(".+@zup.com.br");
        Matcher buscador = padrao.matcher(email);
        boolean eValido = buscador.matches();
        if (!eValido) {
            throw new DominioInvalidoException("Permitido cadastro apenas para email Zup!");
        }
        return true;
    }

    public Usuario localizarUsuarioPorEmail(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        }
        throw new UsuarioNaoExisteException("Usuário não existe");
    }


}
