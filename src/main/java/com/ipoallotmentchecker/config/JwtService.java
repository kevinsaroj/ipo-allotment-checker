package com.ipoallotmentchecker.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

	@Autowired
	private UserRepository repository;
    // Replace this with a secure key in a real application, ideally fetched from environment variables
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

 // Generate token with given user name and userId
    public String generateToken(String userName) {
        // Fetch userId from the authenticated user, for example, from the authentication context
        Long userId = getUserIdFromAuthentication(userName); // You need to implement this method based on your app's logic
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, userId);
    }

 // Create a JWT token with specified claims and subject (user name)
    private String createToken(Map<String, Object> claims, String userName, Long userId) {
        // Add the userId to the claims map
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    
    
    
 // Method to fetch userId (can be customized as per your application's authentication mechanism)
    private Long getUserIdFromAuthentication(String userName) {
        // You can fetch the userId from the database or from the security context
        // For simplicity, assuming userName is linked to a userId directly
      User user = this.repository.findByEmail(userName).get();
    	
    	return user.getId();
    }
    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    // Extract the userId from the token (assuming userId is stored as a claim)
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("sub", String.class));
    }


}