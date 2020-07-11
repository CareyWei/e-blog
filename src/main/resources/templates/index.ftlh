<#include "inc/layout.ftlh"/>
<#--使用宏-->
<@layout "首页">

<#--后头-->
<#include "inc/header-panal.ftlh"/>
<#--内容-->
<div class="layui-container">
  <div class="layui-row layui-col-space15">
    <#--左边-->
    <#include "inc/container-left.ftlh"/>
    <#--右边-->
    <#include "inc/container-right.ftlh"/>
  </div>
</div>
</@layout>