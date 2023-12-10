package wanted.budgetmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wanted.budgetmanagement.config.security.jwt.JwtRequestDto;
import wanted.budgetmanagement.domain.user.dto.AlertRequestDto;
import wanted.budgetmanagement.domain.user.dto.UserRequestDto;
import wanted.budgetmanagement.response.CommonResponse;
import wanted.budgetmanagement.response.ResponseCode;
import wanted.budgetmanagement.service.UserService;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @Operation(summary = "회원 가입", description = "회원 가입")
    @PostMapping("/sign")
    public ResponseEntity<CommonResponse> sign(@Valid @RequestBody UserRequestDto userDto) {

        ResponseCode userCreate = ResponseCode.USER_CREATE;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userCreate)
                        .code(userCreate.getCode())
                        .message(userCreate.getMessage())
                        .data(userService.sign(userDto))
                        .build()
        );
    }

    /**
     * 로그인
     */
    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody JwtRequestDto userDto) {

        ResponseCode userCreate = ResponseCode.USER_LOGIN;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userCreate)
                        .code(userCreate.getCode())
                        .message(userCreate.getMessage())
                        .data(userService.login(userDto))
                        .build()
        );
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public void logout(Authentication auth, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 알람 업데이트
     */
    @Operation(summary = "알람 업데이트", description = "업데이트")
    @PostMapping("/alert")
    public ResponseEntity<CommonResponse> alertUpdate(
            Authentication auth,
            @RequestBody AlertRequestDto alertRequestDto) {

        ResponseCode alertUpdate = ResponseCode.USER_ALERT_UPDATE;

        userService.alertUpdate(auth.getName(), alertRequestDto);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(alertUpdate)
                        .code(alertUpdate.getCode())
                        .message(alertUpdate.getMessage())
                        .build()
        );
    }

}
