package com.liulei.bookservice.common.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET = "runzhe#$%*!()<blog>";

    private static final String EXP = "exp";

    private static final String HEADER = "token";

    private final static int expiresSecond = 24 * 60 * 60 * 10000;

    /**
     * 根据对象生成token
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String sign(T obj) {
        final JWTSigner jwtSigner = new JWTSigner(SECRET);
        final HashMap<String, Object> map = new HashMap<>();
        try {
            String jsonStr = new ObjectMapper().writeValueAsString(obj);
            map.put(HEADER, jsonStr);
            map.put(EXP, System.currentTimeMillis() + expiresSecond);
            return jwtSigner.sign(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    /**
     * 根据token解析对象
     *
     * @param classT
     * @param jwt
     * @param <T>
     * @return
     */
    public static <T> T parse(Class<T> classT, String jwt) {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            final Map<String, Object> claims = verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(HEADER)) {
                long exp = (Long) claims.get(EXP);
                long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(HEADER);
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(json, classT);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
