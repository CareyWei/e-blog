package com.dyzzw.blog.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "post",type = "post",createIndex=true)//索引
public class PostDocment implements Serializable {
    @Id
    private Long id;

    //IK分词器
    @Field(type = FieldType.Text,searchAnalyzer = "ik_smart",analyzer = "ik_max_word")
    private String title;

    private Long authodID;

    @Field(type = FieldType.Keyword)
    private  String authodName;

    private String authodAvatar;

    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;

    //置顶

    private  Integer level;
    //精华
    private Boolean recommend;

    //评论数
    private Integer commentCount;

    //访问量
    private Integer viewCount;

    @Field(type = FieldType.Date)
    private Data created;


}
