package ar.edu.um.programacion2.marcos2504.EventosProxy.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsientoRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public Object obtenerClave(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
