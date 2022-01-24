package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.Exceptions.DominioInvalidoException;
import br.com.zup.ZupNotion.exceptions.EmailJaExistenteException;
import br.com.zup.ZupNotion.exceptions.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void cadastrarUsuario(Usuario usuario) {
        verificarEmailNaoExistente(usuario.getEmail());
        validarEmailZup(usuario.getEmail());
        verificarSenhaForte(usuario.getSenha());
        usuarioRepository.save(usuario);
    }

    public void verificarEmailNaoExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaExistenteException("Email já cadastrado!");
        }
    }

    public void verificarSenhaForte(String senha) {

        boolean tamanhoAdequado = false;
        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        if (senha.length() >= 6) tamanhoAdequado = true;

        for (char caractere : senha.toCharArray()) {
            if (caractere >= '0' && caractere <= '9') {
                achouNumero = true;
            } else if (caractere >= 'A' && caractere <= 'Z') {
                achouMaiuscula = true;
            } else if (caractere >= 'a' && caractere <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        if (!tamanhoAdequado || !achouNumero || !achouMaiuscula || !achouMinuscula || !achouSimbolo) {
            throw new SenhaInvalidaException("Senha inválida");
        }

    }

    public void validarEmailZup(String email) {

        Pattern padrao = Pattern.compile(".+@zup.com.br");
        Matcher buscador = padrao.matcher(email);
        boolean eValido = buscador.matches();
        if (!eValido) {
            throw new DominioInvalidoException("Permitido cadastro apenas para email Zup!");
        }

    }

    public void alterarSenha(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não existe");
        }

        Usuario usuarioBanco = usuarioOptional.get();
        usuarioBanco.setSenha(usuario.getSenha());

        usuarioRepository.save(usuarioBanco);
    }

}


