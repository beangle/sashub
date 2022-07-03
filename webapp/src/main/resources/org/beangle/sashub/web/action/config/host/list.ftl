[#ftl]
[@b.head/]
[@b.grid items=hosts var="host"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="10%"][@b.a href="!info?id=${host.id}"]${host.name!}[/@][/@]
    [@b.col property="ip" title="IP地址" width="15%"]${host.ip}[/@]
    [@b.col property="os" title="操作系统" width="15%"]${host.os}[/@]
    [@b.col property="memory" title="内存" width="10%"]${host.memory}GB[/@]
    [@b.col property="cores" title="CPU核心" width="10%"]${host.cores}[/@]
    [@b.col property="cpu" title="CPU规格" width="35%"]${host.cpu}[/@]
  [/@]
[/@]
[@b.foot/]
