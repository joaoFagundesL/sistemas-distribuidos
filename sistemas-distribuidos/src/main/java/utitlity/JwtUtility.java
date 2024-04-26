package utitlity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtility {

    private static final String SECRET_KEY = "s3cr3t";
    private static final long EXPIRATION_TIME_MS = 86400000; 

    public static String generateToken(String userId, String role) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME_MS);

        return JWT.create()
                .withIssuer("your_issuer") 
                .withSubject(userId) 
                .withClaim("role", role) 
                .withIssuedAt(issuedAt) 
                .withExpiresAt(expiresAt) 
                .sign(Algorithm.HMAC256(SECRET_KEY)); 
    }

    public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer("your_issuer") 
                .build()
                .verify(token); 
    }
}
