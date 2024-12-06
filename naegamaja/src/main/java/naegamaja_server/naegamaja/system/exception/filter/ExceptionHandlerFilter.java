package naegamaja_server.naegamaja.system.exception.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.system.exception.dto.ErrorDto;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RestException exception) {
            ObjectMapper mapper = new ObjectMapper();
            log.error("{Rest Exception}: " + exception.getErrorCode().getMessage());

            response.setStatus(exception.getErrorCode().getStatusCode());
            response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(ErrorDto.ErrorResponse.from(exception.getErrorCode())));
        }
    }

}
