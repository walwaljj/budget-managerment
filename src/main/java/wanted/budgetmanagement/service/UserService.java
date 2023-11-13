package wanted.budgetmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.budgetmanagement.config.security.PasswordEncoderConfig;
import wanted.budgetmanagement.config.security.jwt.JwtRequestDto;
import wanted.budgetmanagement.config.security.jwt.JwtResponseDto;
import wanted.budgetmanagement.config.security.jwt.JwtUtils;
import wanted.budgetmanagement.domain.user.dto.UserRequestDto;
import wanted.budgetmanagement.domain.user.dto.UserResponseDto;
import wanted.budgetmanagement.domain.user.entity.CustomUserDetailsManager;
import wanted.budgetmanagement.domain.user.entity.UserEntity;
import wanted.budgetmanagement.exception.CustomException;
import wanted.budgetmanagement.exception.ErrorCode;
import wanted.budgetmanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoder;
    private final CustomUserDetailsManager manager;
    private final JwtUtils jwtUtils;

    /**
     * 회원가입
     */
    public UserResponseDto sign(UserRequestDto userRequestDto) {

        if (manager.userExists(userRequestDto.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATED_USER);
        }

        if (!userRequestDto.getPassword().equals(userRequestDto.getPasswordReChk())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.passwordEncoder().encode(userRequestDto.getPassword()))
                .email((userRequestDto).getEmail())
                .build();
        return UserResponseDto.fromEntity(userRepository.save(userEntity));

    }

    /**
     * 로그인
     */
    public JwtResponseDto login(JwtRequestDto userDto) {

        if (!manager.userExists(userDto.getUsername())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UserDetails userDetails = manager.loadUserByUsername(userDto.getUsername());

        if (!passwordEncoder.passwordEncoder().matches(userDto.getPassword(), userDetails.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }


        JwtResponseDto userResponseDto = JwtResponseDto.builder().username(userDto.getUsername())
                .token(jwtUtils.generateToken(userDetails)).build();

        log.info("tokenDto = {} ", userResponseDto.getToken());

        return userResponseDto;
    }


    public UserResponseDto findByUserName(String username) {
        return UserResponseDto.fromEntity(userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));

    }

}
