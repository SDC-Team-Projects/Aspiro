package com.capcom.aspiro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_validBearerToken_setsAuthenticationAndContinues() throws Exception {
        when(request.getServletPath()).thenReturn("/api/anything");
        when(request.getHeader("Authorization")).thenReturn("Bearer abc.def.ghi");

        when(jwtService.extractEmail("abc.def.ghi")).thenReturn("user@example.com");
        when(jwtService.isTokenValid("abc.def.ghi", "user@example.com")).thenReturn(true);

        UserDetails userDetails = User.withUsername("user@example.com").password("p").authorities("ROLE_USER").build();
        when(customUserDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("user@example.com");

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilter_missingOrInvalidHeader_doesNotAuthenticate_butContinues() throws Exception {
        when(request.getServletPath()).thenReturn("/api/anything");
        when(request.getHeader("Authorization")).thenReturn(null); // missing

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);

        // Also test invalid prefix
        SecurityContextHolder.clearContext();
        when(request.getHeader("Authorization")).thenReturn("Token something");

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(2)).doFilter(request, response);
    }

    @Test
    void doFilter_authEndpointsBypass_withoutCallingJwt() throws Exception {
        when(request.getServletPath()).thenReturn("/auth/login");

        filter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(jwtService, customUserDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
