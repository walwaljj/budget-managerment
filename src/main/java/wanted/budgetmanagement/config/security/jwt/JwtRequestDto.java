package wanted.budgetmanagement.config.security.jwt;

import lombok.Getter;

@Getter
public class JwtRequestDto {

    private String username;
    private String password;
}
