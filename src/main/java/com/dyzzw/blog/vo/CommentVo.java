package com.dyzzw.blog.vo;

import com.dyzzw.blog.entity.Comment;
import lombok.Data;

@Data
public class  CommentVo  extends Comment {
    private Long authodID;
    private String authodName;
    private String authodAvatar;
    private Long authodCount;

}
