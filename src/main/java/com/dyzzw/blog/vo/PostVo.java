package com.dyzzw.blog.vo;

import com.dyzzw.blog.entity.Post;
import lombok.Data;

@Data
public class PostVo extends Post {
    private Long authodID;
    private String authodName;
    private String authodAvatar;
    private String CategoryName;

}
