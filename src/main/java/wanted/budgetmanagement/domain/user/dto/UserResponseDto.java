package wanted.budgetmanagement.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.user.entity.User;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String username;
    private String email;

    public static UserResponseDto fromEntity(User User){
        return UserResponseDto.builder()
                .username(User.getUsername())
                .email(User.getEmail()).build();
    }
}
