package pro.wtao.framework.security.context;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pro.wtao.framework.security.model.LoginUser;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:37    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Component
public class RedisOnlineUserHolder implements OnlineUserHolder{

    final RedisTemplate<String, LoginUser> redisTemplate;

    public static final String KEY_PREFIX = "wtao:security:onlineUser:";

    public RedisOnlineUserHolder(RedisTemplate<String, LoginUser> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String cacheKey(String jti){
        return KEY_PREFIX.concat(jti);
    }

    @Override
    public LoginUser get(String jti) {
        return redisTemplate.opsForValue().get(cacheKey(jti));
    }

    @Override
    public void put(LoginUser loginUser){
        redisTemplate.opsForValue().set(cacheKey(loginUser.getJti()),loginUser);
    }

    @Override
    public void update(LoginUser loginUser) {
        put(loginUser);
    }

    @Override
    public void remove(String tokenId) {
        redisTemplate.delete(cacheKey(tokenId));
    }
}
