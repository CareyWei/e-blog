<#include "../inc/layout.ftlh"/>

<@layout "博客分类">
  <#include "../inc/header-panal.ftlh"/>
  <div class="layui-container">
    <div class="layui-row layui-col-space15">
      <div class="layui-col-md8">
        <div class="fly-panel" style="margin-bottom: 0;">

          <div class="fly-panel-title fly-filter">
            <a href="/" >综合</a>
            <span class="fly-mid"></span>
            <a href="/?Tid=1">精华</a>
            <span class="fly-filter-right layui-hide-xs">
            <a href="#" class="layui-this">按最新</a>
            <span class="fly-mid"></span>

          </span>
          </div>
          <@posts categoryId=currendCategoryId pn=pn size =2  >
          <ul class="fly-list">

              <#list results.records as post>
                <@postList post></@postList>
              </#list>



          </ul>
            <@paging results></@paging>
          <!-- <div class="fly-none">没有相关数据</div> -->

          </@posts>

        </div>
      </div>
      <#include "../inc/container-right.ftlh"/>
    </div>
  </div>

  <script>
    layui.cache.page = 'jie';
  </script>
</@layout>
