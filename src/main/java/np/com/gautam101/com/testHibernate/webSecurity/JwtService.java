package np.com.gautam101.com.testHibernate.webSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Long expiration;

    //    pehele banabai ek tah secretKey je kar saath Token bantai see kena hotai ee

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    //    Je User login kartai okar data utha kar UserDetails mai rahtai je apana sab ke yata mil jatai
    public String getJwtToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .signWith(getSecretKey())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    // aab Username lebai Token ss
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    //    Token mai vhel sabdata yaniki sab Claims sab ee nikal ke detai sab kuxh Toke ke simple Parse kar ke Token ke data detai
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

//    Token valid xai ki nai uho dekhi nh

    public boolean isTokenValid(String token, UserDetails userDetails) {

            Date expiration = extractClaims(token, Claims::getExpiration);
            String username = extractUsername(token);
            boolean isTokenExpired = expiration.before(new Date());
            return username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired;

    }

}