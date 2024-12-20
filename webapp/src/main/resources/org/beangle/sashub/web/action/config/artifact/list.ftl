[#ftl]
[@b.head/]
[@b.grid items=artifacts var="artifact"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("更新版本",action.multi("updateVersion"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="title" title="标题" width="20%"][@b.a href="!info?id=${artifact.id}" ]${artifact.title}[/@][/@]
    [@b.col title="GAV" ]${artifact.gav(artifact.latestVersion)}[/@]
    [@b.col property="contextPath" title="部署上下文"  width="15%"/]
    [@b.col property="arch" title="面向平台" width="8%"/]
    [@b.col property="resolveSupport" title="需要解析"  width="8%"/]
    [@b.col title="容器要求" width="8%"]
      [#if artifact.jspSupport]jsp&nbsp;[/#if]
      [#if artifact.websocketSupport]websocket[/#if]
    [/@]
  [/@]
[/@]
[@b.foot/]
