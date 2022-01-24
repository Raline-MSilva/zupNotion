package br.com.zup.ZupNotion.usuario;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository <Usuario, String> {
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
