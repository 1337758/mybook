package com.liulei.bookservice.common.util;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class JwtUtils {

    private static final String SECRET = "runzhe#$%*!()<blog>";

    private static final String EXP = "exp";

    private static final String HEADER = "token";

    private final static int expiresSecond = 24 * 60 * 60 * 10000;

    /**
     * 根据对象生成token
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

}
