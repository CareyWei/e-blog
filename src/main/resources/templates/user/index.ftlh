<#include "../inc/layout.ftlh"/>
<@layout "用户中心">
  <div class="layui-container fly-marginTop fly-user-main">

    <@UserLeft level=1></@UserLeft>
    <div class="site-tree-mobile layui-hide">
      <i class="layui-icon">&#xe602;</i>
    </div>
    <div class="site-mobile-shade"></div>

    <div class="site-tree-mobile layui-hide">
      <i class="layui-icon">&#xe602;</i>
    </div>
    <div class="site-mobile-shade"></div>


    <div class="fly-panel fly-panel-user" pad20>
      <!--
      <div class="fly-msg" style="margin-top: 15px;">
        您的邮箱尚未验证，这比较影响您的帐号安全，<a href="activate.html">立即去激活？</a>
      </div>
      -->
      <div class="layui-tab layui-tab-brief" lay-filter="user">
        <ul class="layui-tab-title" id="LAY_mine">
          <li data-type="mine-jie" lay-id="index" class="layui-this">我发的帖（<span>${postCount}</span>）</li>
          <li data-type="collection" data-url="/collection/find/" lay-id="collection">（<span>${CollectID}</span>）</li>
        </ul>
        <div class="layui-tab-content" style="padding: 20px 0;">
          <div class="layui-tab-item layui-show">
            <ul class="mine-view jie-row " id="promulgate">
              <#--使用模板引擎，流加载-->
              <script id="tpl-promulgate" type="text/html">
              <li>
                <a class="jie-title" href="/post/{{d.id}}" target="_blank">{{d.title}}</a>
                <i>{{layui.util.toDateString(d.created,'yyyy-MM-dd HH:mm:ss')}}</i>
                <a class="mine-edit" href="/post/edit?id={{d.id}}">编辑</a>
                <em>{{d.viewCount}}阅/{{d.commentCount}}答</em>
              </li>
              </script>
            </ul>
            <div id="LAY_page"></div>
          </div>
          <div class="layui-tab-item">
            <ul class="mine-view jie-row" id="collection">
              <script id="tpl-collection" type="text/html">
              <li>
                <a class="jie-title" href="/post/{{d.id}}" target="_blank">{{d.title}}</a>
                <i>收藏于{{layui.util.timeAgo(d.created,true)}}</i>  </li>
              </script>
            </ul>
            <div id="LAY_page1"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    layui.cache.page = 'user';
    layui.use(['laytpl','flow','util'],function() {
      var laytpl = layui.laytpl;
      var flow = layui.flow;
      var util = layui.util;
      var $ = layui.jquery;

      flow.load({
        elem: '#promulgate' ,//指定列表容器
        isAuto: false
        ,done: function(page, next){ //到达临界点（默认滚动触发），触发下一页
          var lis = [];
          //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
          $.get('/user/public?pn='+page, function(res){
            //假设你的列表返回在data集合中
            layui.each(res.data.records, function(index, item){
              var  tpl = $("#tpl-promulgate").html();
              laytpl(tpl).render(item,function (html) {
                $("#promulgate .layui-flow-more").before(html)
              })
            });

            //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
            //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
            next(lis.join(''), page < res.data.pages);
          });
        }
      });

      flow.load({
        elem: '#collection' ,//指定列表容器
        isAuto: false
        ,done: function(page, next){ //到达临界点（默认滚动触发），触发下一页
          var lis = [];
          //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
          $.get('/user/collection?pn='+page, function(res){
            //假设你的列表返回在data集合中
            layui.each(res.data.records, function(index, item){

              var  tpl = $("#tpl-collection").html();
              laytpl(tpl).render(item,function (html) {
                $("#collection .layui-flow-more").before(html)
              })
            });

            //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
            //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
            next(lis.join(''), page < res.data.pages);
          });
        }
      });

    });
  </script>

  <script src="/res/js/jquery.min.js"></script>
</@layout>

