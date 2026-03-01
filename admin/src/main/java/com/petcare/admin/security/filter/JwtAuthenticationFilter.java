package com.petcare.admin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.admin.security.application.AccessTokenValidator;
import com.petcare.admin.security.domain.SecurityToken;
import com.petcare.admin.security.domain.StaffUserPrincipal;
import com.petcare.admin.security.repository.SecurityTokenRepository;
import com.petcare.admin.utils.HttpServletRequestUtils;
import com.petcare.common.exception.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AccessTokenValidator accessTokenValidator;
  private final SecurityTokenRepository tokenRepository;
  private final ObjectMapper objectMapper;

  private static final WebAuthenticationDetailsSource AUTH_DETAILS_SOURCE =
      new WebAuthenticationDetailsSource();

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String tokenId = HttpServletRequestUtils.getJwtFromRequest(request);

      if (!StringUtils.hasText(tokenId)) {
        prepareResponse(response, "Missed access token");
        return;
      }

      Optional<SecurityToken> securityTokenOpt = tokenRepository.fetchFullAccessToken(tokenId);

      if (securityTokenOpt.isEmpty()) {
        prepareResponse(response, "Invalid token");
        return;
      }

      SecurityToken securityToken = securityTokenOpt.get();
      String jwt = securityToken.getAccessToken();

      if (!StringUtils.hasText(jwt) || !accessTokenValidator.execute(jwt)) {
        prepareResponse(response, "Invalid token");
        return;
      }

      UserDetails user = new StaffUserPrincipal(securityToken.getStaffUser());

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

      authentication.setDetails(AUTH_DETAILS_SOURCE.buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      log.debug("Set authentication for user: {}", user.getUsername());
      filterChain.doFilter(request, response);

    } catch (RuntimeException ex) {
      log.error("Could not set user authentication in security context", ex);
      prepareResponse(response, "Invalid token");
    }
  }

  private void prepareResponse(HttpServletResponse response, String errorMsg) throws IOException {
    SecurityContextHolder.clearContext();

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    response
        .getWriter()
        .write(
            objectMapper.writeValueAsString(ErrorResponse.of(HttpStatus.UNAUTHORIZED, errorMsg)));
  }
}
