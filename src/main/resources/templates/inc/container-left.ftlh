<div class="layui-col-md8">
    <div class="fly-panel">
        <#--置顶-->
        <div class="fly-panel-title fly-filter">
            <a>置顶</a>
        </div>
        <ul class="fly-list">
            <#list pageDate2.records as post>
                <@postList post></@postList>
            </#list>

        </ul>
    </div>

    <div class="fly-panel" style="margin-bottom: 0;" id="">

        <div class="fly-panel-title fly-filter">
            <a href="/" >综合</a>
            <a href="/?Tid=1"  >精华</a>
            <span class="fly-filter-right layui-hide-xs">
            <a href="#" class="layui-this">按最新</a>
            </span>
        </div>
        <#--综合-->
        <ul class="fly-list">
            <#list pageDate.records as post>
            <@postList post></@postList>
            </#list>

        </ul>

        <@paging pageDate></@paging>

    </div>
</div>
<script type="text/javascript">

   $(function () {
       $("#a1").click(function () {
           $("#a1").addClass("layui-this");
       })
   })

</script>