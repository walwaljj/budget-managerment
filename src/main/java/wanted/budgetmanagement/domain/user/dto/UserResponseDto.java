package wanted.budgetmanagement.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.user.entity.UserEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private String username;
    private String email;

    public static UserResponseDto fromEntity(UserEntity userEntity){
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail()).build();
    }
}
