package np.com.gautam101.com.testHibernate.webSecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilterChain extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        Header nikal
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

//        Bearer se jwt token nikal
        try {
        final String jwtToken = authHeader.substring(7);
        final String email = jwtService.extractUsername(jwtToken);


//        agar email veht gelai aaro user avhi tak authenticate nai vehl xai tah authenticate karu

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails details = userDetailsService.loadUserByUsername(email);

//            token mai valid email aaro token expiry tah nai vhagele check ke laagi



                if (jwtService.isTokenValid(jwtToken, details)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
//                Optional
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//                main kaam yata xai set karu user ke SecurityContextHolder mai

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
                filterChain.doFilter(request,response);



        }
        } catch (Exception e) {

            handlerExceptionResolver.resolveException(request,response,null,e);
            return;
        }


    }
}
