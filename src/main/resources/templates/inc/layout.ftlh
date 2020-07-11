<#macro layout title>
    <!DOCTYPE html>
    <html>
    <#--头部-->
    <head>
        <meta charset="utf-8">
        <title>${title}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <meta name="keywords" content="C博客">
        <meta name="description" content="C博客">
        <link rel="stylesheet" href="/res/layui/css/layui.css">
        <link rel="stylesheet" href="/res/css/global.css">

        <script src="/res/js/jquery.min.js"></script>
        <script src="/res/layui/layui.js"></script>
        <script src="/res/js/sockjs.js"></script>
        <script src="/res/js/stomp.js"></script>

    </head>
    <body>
    <#--分页-->
    <#include "./commen.ftlh">
    <#--前头-->
    <#include "header.ftlh"/>
    <#--不通用的部分可用nested-->
    <#nested >
    <#--尾部-->
    <#include "./footer.ftlh"/>

    <script>
        //     layui.cache.page = '';
        <@shiro.user>

        layui.cache.user = {
            username: '<@shiro.principal property="username"/>'
            , uid: <@shiro.principal property="id"/>
            , avatar: 'http://<@shiro.principal property="avatar"/>'
            , experience: 83
            , sex: '<@shiro.principal property="gender"/> ，"0为女1为男"'
        };
        </@shiro.user>

        <@shiro.guest>
        layui.cache.user = {
            username: '游客'
            , uid: -1
            , avatar: './res/images/avatar/00.jpg'
            , experience: 83
            , sex: '1，"0为女1为男"'
        };
        </@shiro.guest>

        layui.config({
            version: "3.0.0"
            , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
        }).extend({
            fly: 'index'
        }).use('fly');

    </script>
<#--建立通信通道-->
    <script>
        function showTips(count){
            var msg = $('<a class="fly-nav-msg" href="javascript:;">'+ count +'</a>');
            var elemUser = $('.fly-nav-user');
            elemUser.append(msg);
            msg.on('click', function(){

                        location.href = '/user/message/';

            });
            layer.tips('你有 '+ count +' 条未读消息', msg, {
                tips: 3
                ,tipsMore: true
                ,fixed: true
            });
            msg.on('mouseenter', function(){
                layer.closeAll('tips');
            })

        }


        $(function () {
            var elemUser = $('.fly-nav-user');
            if(layui.cache.user.uid !== -1 && elemUser[0]){
                var socket = new SockJS("/webSocket");/*建立通道，与之前注册的webSocket对应*/
                var stompClient = Stomp.over(socket);
                stompClient.connect({},function (frame) {
                    stompClient.subscribe("/user/" + <#if profile??>${profile.id}</#if> + "/messCount/",function (res) {  //res表示监听到的消息
                         //监听之后，弹窗通知
                        console.log(res);
                        showTips(res.body);
                    })
                    
                })
             }
        });

    </script>
    </body>
    </html>
</#macro>