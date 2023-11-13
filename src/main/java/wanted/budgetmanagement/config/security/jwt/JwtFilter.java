package wanted.budgetmanagement.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import wanted.budgetmanagement.domain.user.entity.CustomUserDetails;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorization = request.getHeader("Authorization");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        //만약 header 에 authorization 가 null 이라면
        if(authorization == null){
            // 세션 토큰 확인
            HttpSession session = request.getSession(false);


            if(session == null || session.getAttribute("jwt") == null){
                filterChain.doFilter(request, response);
                return ;
            }

            //만약 header 에 jwt 가 있다면 가져옴.
            else {
                authorization = session.getAttribute("jwt").toString();
            }

        }
        if(authorization != null && authorization.startsWith("Bearer ")){
            String token = authorization.split(" ")[1];

            // 토큰 만료 체크
            if(jwtUtils.validate(token)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                String username = jwtUtils.parseClaims(token).getSubject();

                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        CustomUserDetails.builder().username(username).build(), token, new ArrayList<>());

                context.setAuthentication(authenticationToken);

                SecurityContextHolder.setContext(context);
            }else {
                    log.warn("jwt validation failed");
            }

        }

        filterChain.doFilter(request, response);
    }
}
