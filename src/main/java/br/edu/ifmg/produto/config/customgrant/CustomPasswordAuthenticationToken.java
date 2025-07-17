package br.edu.ifmg.produto.config.customgrant;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.util.Map;

public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	@Serial
	private static final long serialVersionUID = 1L;
	
	private final String username;
	private final String password;

    public CustomPasswordAuthenticationToken(Authentication clientPrincipal,
											 @Nullable Map<String, Object> additionalParameters) {
		
		super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);

        assert additionalParameters != null;
        this.username = (String) additionalParameters.get("username");
		this.password = (String) additionalParameters.get("password");
    }

	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

}
