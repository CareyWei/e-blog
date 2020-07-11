package com.dyzzw.blog.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

        //0成功，1失败
    private int status;
    private String msg;
    private Object data;
    private String action;

    public static  Result success(){

        return Result.success(null,"操作成功");
    }
    public static  Result success(Object data){

        return Result.success(data,"操作成功");
    }
    public static  Result success(Object data,String msg){
        Result r= new Result();
        r.msg=msg;
        r.data=data;
        r.status=0;
        return r;
    }
    public static  Result fail(String msg){
        Result r= new Result();
        r.msg=msg;
        r.data=null;
        r.status=-1;
        return r;
    }
    public Result action(String action){
        this.action=action;
        return this;
    }
}
