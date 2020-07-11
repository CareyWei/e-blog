<div class="layui-col-md4">





    <dl class="fly-panel fly-list-one">
        <dt class="fly-panel-title">本周热议</dt>
        <@hots>

            <#list results as post>

                <dd>
                    <a href="/post/${post.id}"><#if post??>${post.title}</#if></a>
                    <span><i class="iconfont icon-pinglun1"></i> ${post.commentCount}</span>
                </dd>

            </#list>

        </@hots>



        <!-- 无数据时 -->
        <!--
        <div class="fly-none">没有相关数据</div>
        -->
    </dl>


    <div class="fly-panel fly-link">
        <h3 class="fly-panel-title">友情链接</h3>
        <dl class="fly-panel-main">
            <dd><a href="http://www.layui.com/" target="_blank">layui</a><dd>
            <dd><a href="http://1111111/" target="_blank">11111</a><dd>
            <dd><a href="http://22222/" target="_blank">22222</a><dd>
            <dd><a href="http://3333/" target="_blank">3333</a><dd>
            <dd><a href="mailto:xianxin@layui-inc.com?subject=%E7%94%B3%E8%AF%B7Fly%E7%A4%BE%E5%8C%BA%E5%8F%8B%E9%93%BE" class="fly-link">申请友链</a><dd>
        </dl>
    </div>

</div>