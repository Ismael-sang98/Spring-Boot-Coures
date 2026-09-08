package com.ismaeldev.auth_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // L'annuaire pour chercher l'utilisateur en BDD

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. On regarde le "Header" (l'entête) de la requête HTTP qui s'appelle "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si Flutter n'a pas mis d'entête, ou si ça ne commence pas par "Bearer " (le format standard du JWT)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // On laisse passer (le serveur bloquera plus tard si c'est une route sécurisée)
            return;
        }

        // 3. On découpe le texte pour ne garder que le Token (on enlève les 7 premiers caractères : "Bearer ")
        jwt = authHeader.substring(7);

        // 4. On demande à notre machine (JwtService) d'extraire l'email du Token
        userEmail = jwtService.extractUsername(jwt);

        // 5. Si on a trouvé un email ET que l'utilisateur n'est pas encore connecté dans le contexte actuel
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // On va chercher le vrai utilisateur dans la base de données MySQL
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. On demande au JwtService de vérifier si le Token est valide (pas expiré, pas falsifié)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 7. LE TOKEN EST VALIDE ! On crée un "Passe-Droit" officiel pour Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. On donne ce Passe-Droit au gardien principal (SecurityContextHolder).
                // L'utilisateur est officiellement connecté !
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. On dit à la requête de continuer son chemin vers le Contrôleur
        filterChain.doFilter(request, response);
    }
}