package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.Exceptions.DominioInvalidoException;
import br.com.zup.ZupNotion.exceptions.EmailJaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void cadastrarUsuario(Usuario usuario) {
        if (
                validarEmailZup(usuario.getEmail()) &&
                verificarEmailNaoExistente(usuario.getEmail()) &&
                senhaForte(usuario.getSenha())
        )
            usuarioRepository.save(usuario);
    }

    public boolean verificarEmailNaoExistente(String email) {
        if (!usuarioRepository.existsByEmail(email)) {
            return true;
        }
        throw new EmailJaExistenteException("Email já cadastrado!");

    }

    public boolean senhaForte(String senha) {

        if (senha.length() < 6) return false;

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

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

        return achouMaiuscula && achouMinuscula && achouNumero && achouSimbolo;
    }

    public boolean validarEmailZup(String email) {
        boolean dominio = Arrays.stream(email.split("@")).findFirst().equals("zup.com.br");

        if (!dominio) {
            throw new DominioInvalidoException("Permitido cadastro apenas para email Zup!");

        } else {
            return true;
        }
    }

}
