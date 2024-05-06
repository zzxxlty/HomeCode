// SecurityFilter.java
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Decode the Base64-encoded header and extract role information
        String encodedHeader = request.getHeader("Authorization");
        if (encodedHeader != null && encodedHeader.startsWith("Basic ")) {
            String encodedCredentials = encodedHeader.substring(6).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
            String decodedHeader = new String(decodedBytes);
            // Set role information as a request attribute
            request.setAttribute("role", decodedHeader);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
