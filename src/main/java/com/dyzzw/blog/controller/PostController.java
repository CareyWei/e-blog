package com.dyzzw.blog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dyzzw.blog.common.lang.Result;
import com.dyzzw.blog.config.RabbitConfig;
import com.dyzzw.blog.entity.*;
import com.dyzzw.blog.search.mq.PostMqIndexMessage;
import com.dyzzw.blog.util.ValidationUtil;
import com.dyzzw.blog.vo.CommentVo;
import com.dyzzw.blog.vo.PostVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class PostController extends BaseController {

    @GetMapping("/category/{id:\\d*}") //转换成数字类型
    public String category(@PathVariable(name = "id") Long id) {
        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);
        req.setAttribute("pn", pn);
        req.setAttribute("currentCategoryID", id);
        return "post/category";
    }

    @GetMapping("/post/{id:\\d*}") //转换成数字类型
    public String detail(@PathVariable(name = "id") Long id) {
        PostVo vo = postService.selectOnewPost(new QueryWrapper<Post>().eq("p.id", id));
        Assert.notNull(vo, "文章已被删除");

        postService.setViewCount(vo);

        IPage<CommentVo> results = commentService.paging(getPage(), vo.getId(), null, "created");

        req.setAttribute("post", vo);
        req.setAttribute("currentCategoryID", vo.getCategoryId());
        req.setAttribute("pageDate", results);
        return "post/detail";
    }

    /**
     * 查询帖子是否被收藏
     */
    @ResponseBody
    @PostMapping("/collection/find")
    public Result collectionFind(Long pid) {
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfiledID())
                .eq("post_id", pid)
        );

        return Result.success(MapUtil.of("collection", count > 0 ? true : false));

    }

    /**
     * 收藏帖子
     */
    @ResponseBody
    @PostMapping("/collection/add")
    public Result collectionAdd(Long pid) {
        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该帖子已被删除");


        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfiledID())
                .eq("post_id", pid)
        );
        if (count > 0) {
            return Result.fail("该帖子已被收藏");
        }
        UserCollection userCollection = new UserCollection();
        userCollection.setPostId(pid);
        userCollection.setUserId(getProfiledID());
        userCollection.setPostUserId(post.getUserId());
        userCollection.setCreated(new Date());
        userCollection.setModified(new Date());
        userCollectionService.save(userCollection);

        return Result.success();

    }

    /**
     * 删除收藏
     */
    @ResponseBody
    @PostMapping("/collection/remove/")
    public Result collectionRemove(Long pid) {
        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该帖子已被删除");
        userCollectionService.remove(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfiledID())
                .eq("post_id", pid)
        );

        return Result.success();

    }

    /**
     * 发表博客
     */
    @GetMapping("/post/edit")
    public String PostEdid() {
        String id = req.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            Post post = postService.getById(id);
            Assert.isTrue(post != null, "该帖子已被删除");
            Assert.isTrue(post.getUserId() == getProfiledID(), "没有权限操作此文章");
            req.setAttribute("post", post);
        }
        req.setAttribute("cetagory", categoryService.list());
        return "post/edit";
    }

    /**
     * 提交修改编辑
     *
     * @param post
     * @return
     */

    @ResponseBody
    @PostMapping("/post/submit")
    public Result submit(Post post) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(post);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }
        if (post.getId() == null) {
            post.setUserId(getProfiledID());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(null);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            postService.save(post);

        } else {
            Post tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId().longValue() == getProfiledID().longValue(), "无权限编辑此文章！");

            tempPost.setTitle(post.getTitle());
            tempPost.setContent(post.getContent());
            tempPost.setCategoryId(post.getCategoryId());
            postService.updateById(tempPost);
        }
        //发送消息给MQ，告知更新或添加
        amqpTemplate.convertAndSend(RabbitConfig.ES_CHANGE,RabbitConfig.ES_BINDING_KEY,
                new PostMqIndexMessage(post.getId(),PostMqIndexMessage.CREATE_OR_UPDATE));

        return Result.success().action("/post/" + post.getId());
    }

    /**
     * 删除博客
     *
     * @param id
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/post/delete")
    public Result delete(Long id) {
        Post post = postService.getById(id);
        Assert.notNull(post, "该帖子已被删除");
        Assert.isTrue(post.getUserId() == getProfiledID(), "无权限删除此文章");
        postService.removeById(id);
        //删除相关消息、收藏
        userMessageService.removeByMap(MapUtil.of("post_id", id));
        userCollectionService.removeByMap(MapUtil.of("post_id", id));
        //发送消息给MQ，告知删除
        amqpTemplate.convertAndSend(RabbitConfig.ES_CHANGE,RabbitConfig.ES_BINDING_KEY,
                new PostMqIndexMessage(post.getId(),PostMqIndexMessage.REMOVE));

        return Result.success().action("/index");

    }

    /**
     * 提交评论、即时通知
     * @param jid
     * @param content
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/post/reply")
    public Result delete(Long jid, String content) {
        Assert.notNull(jid, "该帖子已被删除");
        Assert.hasLength(content, "评论内容不等于空");


        Post post = postService.getById(jid);
        Assert.isTrue(post != null, "该文章已被删除");

        Comment comment = new Comment();
        comment.setPostId(jid);
        comment.setContent(content);
        comment.setUserId(getProfiledID());
        comment.setCreated(new Date());
        comment.setModified(new Date());
        comment.setLevel(0);
        comment.setVoteDown(0);
        comment.setVoteUp(0);
        commentService.save(comment);
        //评论数量加一
        post.setCommentCount(post.getCommentCount() + 1);
        postService.updateById(post);

        //本周热议数量加一
        postService.incrCommentCountAndUnionForWeekRank(post.getId(), true);

        //评论消息通知
        //通知作者，有人评论了你的文章
        //如果是作者自己评论则不需要通知
        if (comment.getUserId() != post.getUserId()) {
            UserMessage message = new UserMessage();
            message.setPostId(jid);
            message.setCommentId(comment.getId());
            message.setFromUserId(getProfiledID());
            message.setToUserId(post.getUserId());
            message.setType(1);
            message.setContent(content);
            message.setCreated(new Date());
            //查看当前评论是否是未读
            message.setStatus(0);
            userMessageService.save(message);
            //即时通知作者
            webService.sendMsgCountToUser(message.getToUserId());
        }
        //通知被@的人，有人回复了你的评论
        if (content.startsWith("@")) {
            String username = content.substring(1, content.indexOf(" "));
            System.out.println(username);
            User user = userService.getOne(new QueryWrapper<User>()
                    .eq("username", username));

            if (user != null) {
                UserMessage message = new UserMessage();
                message.setPostId(jid);
                message.setCommentId(comment.getId());
                message.setFromUserId(getProfiledID());
                message.setToUserId(user.getId());
                message.setType(2);
                message.setContent(content);
                message.setCreated(new Date());
                //查看当前评论是否是未读
                message.setStatus(0);
                userMessageService.save(message);
            }
        }

        return Result.success().action("/post/" + post.getId());

    }

    @ResponseBody
    @Transactional
    @PostMapping("/post/reply-delete")
    public Result deleteReply(Long id){

        Assert.notNull(id,"评论ID不能为空");
        Comment comment = commentService.getById(id);

        Assert.notNull(comment,"找不到对应的评论");

    if(comment.getUserId()!=getProfiledID()){
        return Result.fail("不能删除不是本人的评论");
    }
    commentService.removeById(id);
    //评论数量减一
        Post post = postService.getById(comment.getPostId());
        post.setCommentCount(post.getCommentCount()-1);
        postService.updateById(post);
        //缓存评论数量减一
        postService.incrCommentCountAndUnionForWeekRank(comment.getPostId(),false);
       return Result.success();
    }

}
