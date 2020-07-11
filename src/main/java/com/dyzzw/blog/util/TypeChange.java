package com.dyzzw.blog.util;

import com.dyzzw.blog.search.model.PostDocment;
import com.dyzzw.blog.vo.PostVo;
import org.modelmapper.PropertyMap;

public class TypeChange {

    public  static   PropertyMap customField(){
        /**
         * 自定义映射规则
         */
        return  new PropertyMap<PostVo, PostDocment>() {
            @Override
            protected void configure() {
                /**使用自定义转换规则*/
                map(source.getCreated(),destination.getCreated());
            }
        };
    }

}
