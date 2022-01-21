package br.com.zup.ZupNotion.usuario;

import br.com.zup.ZupNotion.Exceptions.EmailJaExistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario (Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario verificarEmailExistente(Usuario usuario){
        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new EmailJaExistenteException("Email já cadastrado!");
        }
        return usuarioRepository.save(usuario);
    }

    public boolean senhaForte(String senha) {

        if (senha.length() >= 6) {

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

            if (achouMaiuscula && achouMinuscula && achouNumero && achouSimbolo) {
                return true;
            } else {
                return false;
            }
        }
        throw new SenhaInvalidaException("A senha deve conter no mínimo 6 caracters");

    }

}
