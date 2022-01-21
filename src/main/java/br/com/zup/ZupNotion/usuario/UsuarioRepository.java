package br.com.zup.ZupNotion.usuario;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository <Usuario, String> {
    boolean existsByEmail(String email);

}
