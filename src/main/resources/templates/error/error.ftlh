




<#include "../inc/layout.ftlh"/>
<#--使用宏-->
<@layout "错误提示">

  <style>
    .notice{width: 600px; margin: 30px auto; padding: 30px 15px; border-top: 5px solid #009688; line-height: 30px;  text-align: center; font-size: 16px; font-weight: 300; background-color: #f2f2f2;}

    @media screen and (max-width: 750px) {
      html body{margin-top: 0;}
      .notice{width: auto; margin: 20px 15px; padding: 30px 0;}
    }
  </style>

  <div class="fly-none" style="min-height: 0; padding: 0;">
    <i class="iconfont icon-tishilian"></i>
  </div>

  <div class="notice layui-text">
    <p>${message}</p>
  </div>


</@layout>
