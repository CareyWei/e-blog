<#include "inc/layout.ftlh" />

<@layout "搜索 - ${q}">

<#include "inc/header-panal.ftlh" />

<div class="layui-container">
    <div class="layui-row layui-col-space15">

        <div class="layui-col-md8">
            <div class="fly-panel">

                <div class="fly-panel-title fly-filter">
                    <a>您正在搜索关键字 “ ${q} ” - 共有 <strong><#if pageData??>${pageData.total}<#else>0</#if></strong> 条记录</a>
                    <a href="#signin" class="layui-hide-sm layui-show-xs-block fly-right" id="LAY_goSignin" style="color: #FF5722;">去签到</a>
                </div>
                <ul class="fly-list">

                    <#list pageData.records as post>
                        <li>
                            <a href="/post/${post.authodID}" class="fly-avatar">
                                <img src="http://${post.authodAvatar}" alt="${post.authodName}">
                            </a>
                            <h2>
                                <a class="layui-badge">${post.categoryName}</a>
                                <a href="/post/${post.id}">${post.title}</a>
                            </h2>
                            <div class="fly-list-info">
                                <a href="/post/${post.authodID}" link>
                                    <cite>${post.authodName}</cite>
                                    <!--
                                    <i class="iconfont icon-renzheng" title="认证信息：XXX"></i>
                                    <i class="layui-badge fly-badge-vip">VIP3</i>
                                    -->
                                </a>
                                <span class="fly-list-nums">
                <i class="iconfont icon-pinglun1" title="回答"></i> ${post.commentCount}
              </span>
                            </div>
                            <div class="fly-list-badge">
                                <#if post.recommend??&&post.recommend><span class="layui-badge layui-bg-red">精帖</span></#if>
                                <#if post.level gt 0>  <span class="layui-badge layui-bg-black">置顶</span></#if>

                            </div>
                        </li>
                    </#list>

                </ul>
                <@paging pageData></@paging>

            </div>
        </div>

        <#include "inc/container-right.ftlh" />

    </div>
</div>
</@layout>