package wanted.budgetmanagement.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;


@Getter
public class UserRequestDto {

    @NotEmpty(message = "사용자ID는 빈 값일 수 없습니다.")
    @Schema(description = "사용자 계정", example = "user1")
    private String username;
    @NotEmpty(message = "비밀번호는 빈 값일 수 없습니다.")
    @Schema(description = "사용자 비밀번호", example = "123")
    private String password;
    @NotEmpty(message = "비밀번호는 확인은 빈 값일 수 없습니다.")
    @Schema(description = "사용자 비밀번호 확인", example = "123")
    private String passwordReChk;
    @Email(message = "이메일 형식이 아닙니다.")
    @Schema(description = "사용자 이메일", example = "user1@example.com")
    private String email;
}
