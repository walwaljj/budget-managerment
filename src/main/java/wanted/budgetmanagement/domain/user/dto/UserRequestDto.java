package wanted.budgetmanagement.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;


@Getter
public class UserRequestDto {

    @NotEmpty(message = "사용자ID는 빈 값일 수 없습니다.")
    private String username;
    @NotEmpty(message = "비밀번호는 빈 값일 수 없습니다.")
    private String password;
    @NotEmpty(message = "비밀번호는 확인은 빈 값일 수 없습니다.")
    private String passwordReChk;
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
}
