[#ftl]
[@b.head/]
[@b.grid items=servers var="server"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" ][@b.a href="!info?id=${server.id}"]${server.name!}[/@][/@]
    [@b.col property="host.name" title="主机" width="8%"]${server.host.name}[/@]
    [@b.col property="httpPort" title="http端口" width="8%"/]
    [@b.col property="maxHeapSize" title="堆上限" width="8%"]<span title="${server.opts!}">[#if server.maxHeapSize=0]--[#else]${server.maxHeapSize!}MB[/#if]</span>[/@]
    [@b.col property="enableAccessLog" title="访问日志" width="8%"]${server.enableAccessLog?string('启用','忽略')}[/@]
    [@b.col property="proxyOptions" title="代理" width="8%"]
      [#if server.proxyHttpPort??]端口:${server.proxyHttpPort}&nbsp;[/#if]
      ${server.proxyOptions!}
    [/@]
  [/@]
[/@]
[@b.foot/]
