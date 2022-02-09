package br.com.zup.ZupNotion.repositories;

import br.com.zup.ZupNotion.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository <Usuario, String> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findAll(Pageable pageable);
}
