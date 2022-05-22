[#ftl]
[@b.head/]
[@b.grid items=artifacts var="artifact"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="title" title="标题" width="20%"/]
    [@b.col property="artifactId" title="名称"  width="25%"][@b.a href="!info?id=${artifact.id}" ]${artifact.artifactId!}[/@][/@]
    [@b.col property="groupId" title="分组"  width="25%"/]
    [@b.col property="latestVersion" title="最新版本"  width="15%"/]
    [@b.col property="resolveSupport" title="需要解析"  width="10%"/]
  [/@]
[/@]
[@b.foot/]
