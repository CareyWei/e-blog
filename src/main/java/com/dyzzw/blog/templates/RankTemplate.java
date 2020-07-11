package com.dyzzw.blog.templates;

import com.dyzzw.blog.common.templates.DirectiveHandler;
import com.dyzzw.blog.common.templates.TemplateDirective;
import com.dyzzw.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 本周热议
 */
@Component
public class RankTemplate extends TemplateDirective {
   @Autowired
    RedisUtil redisUtil;

    @Override
    public String getName() {
        return "hots";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
       String key ="week:rank";
        Set<ZSetOperations.TypedTuple> zSetRank = redisUtil.getZSetRank(key, 0, 6);
        List<Map> list = new ArrayList<>();

        for (ZSetOperations.TypedTuple t:zSetRank) {
            Map<String,Object> map = new HashMap<>();
            Object value = t.getValue();
            String titleKey= "rank:post"+value;
            map.put("id",value);
            map.put("title",redisUtil.hget(titleKey,"post:title"));
            map.put("commentCount",t.getScore());
            list.add(map);
        }
        handler.put(RESULTS,list).render();
    }
}
