package wanted.budgetmanagement.config.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class JwtRequestDto {

    @Schema(description = "사용자 계정", example = "user1")
    private String username;
    @Schema(description = "사용자 비밀번호", example = "123")
    private String password;
}
