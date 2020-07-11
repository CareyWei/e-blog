package com.dyzzw.blog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.entity.Post;
import com.dyzzw.blog.entity.User;
import com.dyzzw.blog.entity.UserCollection;
import com.dyzzw.blog.entity.UserMessage;
import com.dyzzw.blog.shiro.AccountProfile;
import com.dyzzw.blog.util.QiniuUtil;
import com.dyzzw.blog.vo.UserMessageVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController  extends BaseController{

    @Autowired
    QiniuUtil qiniuUtils;
    /**
     * 基本设置
     * @return
     */
    @GetMapping("user/set")
    public String set(){

        User user =userService.getById(getProfiledID());
        req.setAttribute("user",user);
        return "/user/set";
    }
    @ResponseBody
    @PostMapping("/user/set")
    public Result doSet(User user){
        if(StrUtil.isNotBlank(user.getAvatar())){
            User temp = userService.getById(getProfiledID());
            temp.setAvatar(user.getAvatar());

            boolean a = userService.updateById(temp);
            System.out.println("bbbbbb------------"+a);
            AccountProfile profile =getProfil();
            profile.setAvatar(temp.getAvatar());
            SecurityUtils.getSubject().getSession().setAttribute("profile",profile);
            return Result.success().action("/user/set#avatar");

        }



        if(StrUtil.isBlank(user.getUsername())){
            return Result.fail("昵称不能为空");
        }
        int count = userService.count(new QueryWrapper<User>()
                .eq("username",getProfil().getUsername())
                .ne("id",getProfil().getId())   );
        if(count>0){
            return Result.fail("昵称已存在");
        }

        User temp =userService.getById(getProfiledID());
        temp.setGender(user.getGender());
        temp.setUsername(user.getUsername());
        temp.setSign(user.getSign());
        boolean a = userService.updateById(temp);
        System.out.println("------------"+a);
        AccountProfile profile =getProfil();
        profile.setUsername(temp.getUsername());
        profile.setSign(temp.getSign());
        SecurityUtils.getSubject().getSession().setAttribute("profile",profile);

        return Result.success().action("/user/set#info");
    }

    /**
     * 发表的文章
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/user/public")
    public Result UserPublic(){
        IPage page = postService.page(getPage(), new QueryWrapper<Post>()
                .eq("user_id", getProfiledID())
                .orderByAsc("created"));
        return  Result.success(page);
    }

    /**
     * 收藏的文章
     * @return
     */
    @ResponseBody
    @GetMapping("/user/collection")
    public Result collection(){
        IPage page = postService.page(getPage(), new QueryWrapper<Post>()
                .inSql("id", "select post_id from user_collection where user_id=" + getProfiledID()));

        return    Result.success(page);
    }


    /**
     * 用户中心
     * @return
     */
    @GetMapping("/user/index")
    public String index(Long id){
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
        .eq("user_id",id));
        int countT = postService.count(new QueryWrapper<Post>()
        .eq("user_id",id));
        req.setAttribute("CollectID",count);
        req.setAttribute("postCount",countT);
        return "/user/index";
    }

    /**
     * 我的消息
     * @return
     */
    @GetMapping("/user/message")
    public String message(){
        //获取发送方的ID
     IPage<UserMessageVo> page = userMessageService.paging(getPage(), new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfiledID())
                .orderByAsc("created"));
        //把消息改成已读状态
        List<Long> ids = new ArrayList<>();
        for(UserMessageVo messageVo:page.getRecords()){
            if(messageVo.getStatus()==0){
                ids.add(messageVo.getId());
            }
        }
        //批量修改已读
        userMessageService.updateToRead(ids);
        req.setAttribute("Messages",page);
        return "/user/message";
    }
    /******************
     * 删除消息
     */
        @ResponseBody
        @Transactional
        @PostMapping("/msg/remove")
        public Result msgRemove(Long id ,@RequestParam(defaultValue = "false") Boolean all){

        boolean remove =    userMessageService.remove(new QueryWrapper<UserMessage>()
            .eq("to_user_id",getProfiledID())
            .eq(!all,"id",id));
            return  remove?Result.success():Result.fail("删除失败");
        }
    /**
     * 新消息通知
     */
    @ResponseBody
    @RequestMapping("/message/nums")
    public Map msgNum(){
      int count=  userMessageService.count(new QueryWrapper<UserMessage>()
        .eq("to_user_id",getProfiledID())
        .eq("status","0"));
      return MapUtil.builder("status",0)
                    .put("count",count).build();
    }

    /**
     * 我的主页
     * @return
     */
    @GetMapping("/user/home")
    public String home(){
        User user =userService.getById(getProfiledID());
            List<Post> posts=postService.list(new QueryWrapper<Post>()
        .eq("user_id",user.getId())
        .gt("created", DateUtil.offsetDay(new Date(),-30))//30天内
        .orderByDesc("created"));
        req.setAttribute("user",user);
        req.setAttribute("ps",posts);
        return "/user/home";
    }
    /**
     * 图片上传
     */

    @ResponseBody
    @PostMapping("/user/upload")
    public Result postUserInforUpDate(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("username");
        if (!file.isEmpty()) {
            FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String fileExtend = originalFilename.substring(originalFilename.lastIndexOf("."));
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            String fileKey = UUID.randomUUID().toString().replace("-", "") + fileExtend;
            String data = qiniuUtils.upload(fileInputStream, fileKey);

            return Result.success(data);
        }
        return Result.fail("上传失败");
    }

    /**
     * 重置密码
     * @param olderpwd
     * @param password
     * @param repass
     * @return
     */

    @ResponseBody
    @PostMapping("/user/repass")
    public Result Repassword(String olderpwd, String password, String repass) {
        if(!password.equals(repass)) {
            return Result.fail("两次密码不相同");
        }

        User user = userService.getById(getProfiledID());

        String nowPassMd5 = SecureUtil.md5(olderpwd);
        if(!nowPassMd5.equals(user.getPassword())) {
            return Result.fail("密码不正确");
        }

        user.setPassword(SecureUtil.md5(password));
        userService.updateById(user);

        return Result.success().action("/user/set#pass");

    }


}
