package com.assess.Common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    //公用密钥-保存在服务端,以防被攻击
    public static String SECRET = "zheJingSchool";

    //生成Token
    public static String createToken(String userId) {
        //签发时间
        Date iatDate = new Date();
        //过期时间  120分钟后过期
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 120);
        Date expiresDate = nowTime.getTime();

        Map<String, Object> map = new HashMap();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                    .withHeader(map)
                    .withExpiresAt(expiresDate)//设置过期时间,过期时间要大于签发时间
                    .withIssuedAt(iatDate)//设置签发时间
                    .withAudience(userId) //设置 载荷 签名的用户
                    .sign(Algorithm.HMAC256(SECRET));//加密
        System.out.println("后台生成token:" + token);


        return token;
    }

    //校验TOKEN
    public static boolean verifyToken(String token) throws UnsupportedEncodingException{

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        try {
            verifier.verify(token);

            return true;
        } catch (Exception e){
            return false;
        }
    }

    //获取Token信息
    public static DecodedJWT getTokenInfo(String token) throws UnsupportedEncodingException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        try{
            return verifier.verify(token);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 判断是否需要刷新TOKEN
     * @param issueAt token签发日期
     * @return 是否需要刷新TOKEN
     */
   /* private boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }*/

}

