package br.com.zup.ZupNotion.config.security.JWT;

import br.com.zup.ZupNotion.config.security.JWT.exceptions.AcessoNegadoException;
import br.com.zup.ZupNotion.models.dtos.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@AllArgsConstructor
public class FiltroDeAutenticacaoJWT extends UsernamePasswordAuthenticationFilter {

    private JWTComponent jwtComponent;
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LoginDTO login = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    login.getEmail(), login.getSenha(), new ArrayList<>()
            );

            Authentication autenticacao = authenticationManager.authenticate(authToken);
            return autenticacao;
        } catch (IOException e) {
            throw new AcessoNegadoException();
        }
    }

}
