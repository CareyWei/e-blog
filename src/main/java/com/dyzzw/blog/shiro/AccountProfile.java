package com.dyzzw.blog.shiro;




import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AccountProfile  implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String gender;
    private String sign;
    private String sex="0".equals(gender)?"女":"男";
    private String avatar;

    private  Date created;

    public String getSex(){
        return "0".equals(gender)?"女":"男";
    }

}
