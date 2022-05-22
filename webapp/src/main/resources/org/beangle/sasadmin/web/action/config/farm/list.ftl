[#ftl]
[@b.head/]
[@b.grid items=farms var="farm"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="8%"][@b.a href="!info?id=${farm.id}"]${farm.name!}[/@][/@]
    [@b.col property="engine.name" title="引擎" width="12%"/]
    [@b.col property="maxHeapSize" title="堆上限" width="8%"]<span title="${farm.opts!}">${farm.maxHeapSize!}MB</span>[/@]
    [@b.col property="http.maxThreads" title="最大线程" width="8%"/]
    [@b.col title="服务" width="52%"][#list farm.servers?sort_by('name') as s]${s.host.name}:${s.httpPort}[#sep]&nbsp;[/#list][/@]
    [@b.col title="操作" width="7%"][@b.a href="server!search?server.farm.id="+farm.id target="_blank"]维护[/@][/@]
  [/@]
[/@]
[@b.foot/]
