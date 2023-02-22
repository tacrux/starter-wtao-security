package pro.wtao.framework.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * <b>JWT工具类</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:32    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class JwtUtils {
    /**
     * 引入apollo的配置
     */
    private static String TOKEN_KEY = "tacrux-token-key";
    /**
     * jwt有效时间
     */
    private static long TOKEN_TIMEOUT = 10 * 60 * 1000;

    /**
     * jwt生成方
     */
    private final static String JWT_ISSUER = "tacrux";

    /**
     * 生成jwt
     *
     * @param header
     * @return
     */
    public static String create(Map<String, Object> header, String subject) {
        return create(header, new HashMap<>(0), subject, JWT_ISSUER, TOKEN_TIMEOUT, null);
    }

    public static String create(Map<String, Object> header, Map<String, String> claims, String subject) {
        return create(header, claims, subject, JWT_ISSUER, TOKEN_TIMEOUT, null);
    }

    public static String create(Map<String, Object> header, long timeout, String subject) {
        return create(header, new HashMap<>(0), subject, JWT_ISSUER, timeout, null);
    }

    public static String create(long timeout, String subject, String jti) {
        return create(Collections.emptyMap(), Collections.emptyMap(), subject, JWT_ISSUER, timeout, jti);
    }

    /**
     * 生成jwt
     *
     * @param header
     * @param issuer
     * @return
     */
    public static String create(Map<String, Object> header, Map<String, String> claims, String subject, String issuer, long timeout, String jti) {
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
            Date date = new Date(System.currentTimeMillis() + timeout);
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(header)
                    .withIssuer(issuer)
                    .withExpiresAt(date)
                    .withSubject(subject)
                    .withJWTId(jti);
            for (String key : claims.keySet()) {
                builder.withClaim(key, claims.get(key));
            }
            token = builder.sign(algorithm);
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        return token;
    }

    /**
     * 验证jwt
     *
     * @param token
     * @return
     */
    public static DecodedJWT decode(String token) {
        DecodedJWT jwt;
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(JWT_ISSUER)
//                    .acceptLeeway(1)
//                    .acceptExpiresAt(1)
//                    .acceptIssuedAt(1)
//                    .acceptNotBefore(1)
                .build();
        jwt = verifier.verify(token);
        return jwt;
    }
}
