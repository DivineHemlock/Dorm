package com.airbyte.dorm.manager;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.ManagerDTO;
import com.airbyte.dorm.model.people.Manager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/manager")
@CrossOrigin("*")
@Slf4j
public class ManagerController extends ParentController<Manager, ManagerService, ManagerDTO> {
    public ManagerController(ManagerService service) {
        super(service);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            try {
                String refresh_token = authorizationHeader;
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Manager manager = service.findManagerByUsername(username);
                ArrayList<String> roles = new ArrayList<>();
                roles.add(manager.getRole());
                String accessToken = JWT.create()
                        .withSubject(manager.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() +  60 * 1000 ))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles" , roles)
                        .sign(algorithm);
                response.setHeader("accessToken" , accessToken);
            }
            catch (Exception e) {
                log.error("error login in {} ", e.getMessage());
                response.setHeader("error" , e.getMessage());
            }
        }
        else {
            throw new RuntimeException("refresh token is missing!");
        }
    }
}
