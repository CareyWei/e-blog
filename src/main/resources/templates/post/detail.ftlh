
<#include "../inc/layout.ftlh"/>
<#--使用宏-->
<@layout "博客细节">

<#--后头-->
<#include "../inc/header-panal.ftlh"/>
<div class="layui-container">
  <div class="layui-row layui-col-space15">
    <div class="layui-col-md8 content detail">
      <div class="fly-panel detail-box">
        <h1>${post.title}</h1>
        <div class="fly-detail-info">
          <!-- <span class="layui-badge">审核中</span> -->
          <span class="layui-badge layui-bg-green fly-detail-column">${post.categoryName}</span>
          

        <#if post.level gt 0>  <span class="layui-badge layui-bg-black">置顶</span></#if>
        <#if post.recommend>     <span class="layui-badge layui-bg-red">精帖</span></#if>
          
          <div class="fly-admin-box" data-id="${post.id}">
            <#--发布者删除-->
            <@shiro.lacksRole name="admin">
            <#if profile??&&post.userId==profile.id>
            <span class="layui-btn layui-btn-xs jie-admin" type="del">删除</span>
            </#if>
            </@shiro.lacksRole>
            <#--管理员删除-->
            <@shiro.hasRole name="admin">
              <span class="layui-btn layui-btn-xs jie-admin"  rank="1" field="delete" type="set">删除</span>
                <#if post.level ==0>
              <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="1">
                  置顶
              </span></#if>
                <#if post.level gt 0>
           <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="0" style="background-color:#ccc;">
               取消置顶
              </span></#if>

            <#if !post.recommend>
              <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="1">加精</span>
            </#if>
                <#if post.recommend>
                <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="0" style="background-color:#ccc;">取消加精</span>
                </#if>
            </@shiro.hasRole>
        </div>
          <span class="fly-list-nums"> 
            <a href="#comment"><i class="iconfont" title="回答">&#xe60c;</i> ${post.commentCount}</a>
            <i class="iconfont" title="人气">&#xe60b;</i>  ${post.viewCount}
          </span>
        </div>
        <div class="detail-about">
          <a class="fly-avatar" href="<#if profile??&&post.authodID==profile.id>/user/set#avatar}<#else>#</#if> ">
            <img src="http://${post.authodAvatar}" alt=" ${post.authodName}">
          </a>
          <div class="fly-detail-user">
            <a href="<#if profile??&&post.authodID==profile.id>/user/index?${post.authodID}<#else>#</#if>" class="fly-link">
              <cite> ${post.authodName}</cite>
              <i class="iconfont icon-renzheng" title="认证信息：{{ rows.user.approve }}"></i>
              <i class="layui-badge fly-badge-vip">VIP3</i>
            </a>
            <span>${timeAgo(post.created)}</span>
          </div>
          <div class="detail-hits" id="LAY_jieAdmin" data-id="${post.id}">
            <#if profile??&&post.userId==profile.id>
            <span class="layui-btn layui-btn-xs jie-admin" type="edit"><a href="/post/edit?id=${post.id}">编辑此贴</a></span>
            </#if>
          </div>
        </div>
        <#--内容-->
        <div class="detail-body photos">
          ${post.content}"
        </div>
      </div>
  <#--评论-->
      <div class="fly-panel detail-box" id="flyReply">
        <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
          <legend>回帖</legend>
        </fieldset>

        <ul class="jieda" id="jieda">
          <#list pageDate.records as comment>
            <li data-id="${comment.id}" class="jieda-daan">
              <a name="item-${comment.id}"></a>
              <div class="detail-about detail-about-reply">
                <a class="fly-avatar" href="/user/${comment.authodID}">
                  <img src="http://${comment.authodAvatar}" alt="${comment.authodName} ">
                </a>
                <div class="fly-detail-user">
                  <a href="/user/${comment.authodID}" class="fly-link">
                    <cite>${comment.authodName}</cite>
                  </a>

                <#if comment.userId ==post.userId>
                  <span>(楼主)</span>
                </#if>
                </div>

                <div class="detail-hits">
                  <span>${timeAgo(comment.created)}</span>
                </div>

              </div>
              <div class="detail-body jieda-body photos">
               ${comment.content}
              </div>
              <div class="jieda-reply">
              <span class="jieda-zan zanok" type="zan">
                <i class="iconfont icon-zan"></i>
                <em>${comment.voteUp}</em>
              </span>
                <span type="reply">
                <i class="iconfont icon-svgmoban53"></i>
                回复
              </span>
                <#if profile??&&comment.authodID==profile.id>
                <div class="jieda-admin">
                  <span type="del">删除</span>
                </div>
                </#if>
              </div>
            </li>
          </#list>



          
          <!-- 无数据时 -->
          <!-- <li class="fly-none">消灭零回复</li> -->
        </ul>
          <@paging pageDate></@paging>
        <div class="layui-form layui-form-pane">
          <form action="/post/reply" method="post">
            <div class="layui-form-item layui-form-text">
              <a name="comment"></a>
              <div class="layui-input-block">
                <textarea id="L_content" name="content" required lay-verify="required" placeholder="请输入内容"  class="layui-textarea fly-editor" style="height: 150px;"></textarea>
              </div>
            </div>
            <div class="layui-form-item">
              <input type="hidden" name="jid" value="${post.id}">
              <button class="layui-btn" lay-filter="*" lay-submit alert="true">提交回复</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <#include "../inc/container-right.ftlh"/>
  </div>
</div>


<script>
  layui.cache.page = 'jie';
$(function () {
  layui.use(['fly','face'],function () {
    var fly =layui.fly;
    $('.detail-body').each(function(){
      var othis = $(this), html = othis.html();
      othis.html(fly.content(html));
    });
  });
});

</script>
</@layout>

