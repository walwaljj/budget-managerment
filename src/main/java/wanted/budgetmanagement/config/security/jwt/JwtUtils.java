package wanted.budgetmanagement.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key singleKey;
    private final JwtParser parser;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        this.singleKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.parser = Jwts.parserBuilder()
                .setSigningKey(this.singleKey)
                .build();
    }

    public String generateToken(UserDetails userDetails) {

        Claims claims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(1200)));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(singleKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    public boolean validate(String token) {

        try{
            parser.parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isExpired(String token) {
        Date expiredDate = parseClaims(token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }
}
