package com.ismaeldev.auth_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
@Service
public class JwtService {
    // C'est le "tampon" secret de l'hôtel. (En entreprise, on cache ça dans application.properties)
    // Cette clé DOIT faire au moins 256 bits (très longue). Ne la changez pas pour l'exercice.
    private static final String CLE_SECRETE = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // 1. MÉTHODE POUR EXTRAIRE L'EMAIL DU BADGE (Le scanner du videur)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    // 2. MÉTHODE POUR IMPRIMER UN NOUVEAU BADGE (Pour la réceptionniste)
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername()) // On met l'email dans le badge
                .setIssuedAt(new Date(System.currentTimeMillis())) // Date de création : Maintenant
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Expire dans 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // On tamponne avec la clé secrète !
                .compact();
    }
    // 3. MÉTHODE POUR VÉRIFIER SI LE BADGE EST UN FAUX OU S'IL EST EXPIRÉ
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    // --- Les petites méthodes utilitaires "sous le capot" ---
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(CLE_SECRETE);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}