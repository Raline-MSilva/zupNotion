package br.com.zup.ZupNotion.config.security.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTComponent {
    @Value("${jwt.segredo}")
    private String segredo;
    @Value("${jwt.milissegundos}")
    private Long tempoLogado;

    public String gerarToken(String username, String id){
        Date vencimento = new Date(System.currentTimeMillis()+tempoLogado);

        String token = Jwts.builder().setSubject(username)
                .claim("idUsuario", id).setExpiration(vencimento)
                .signWith(SignatureAlgorithm.HS512, segredo.getBytes()).compact();

        return token;
    }
}
