[#ftl]
[@b.head/]
[@b.grid items=engines var="engine"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称"][@b.a href="!info?id=${engine.id}"]${engine.name!}[/@][/@]
    [@b.col property="typ" title="类型"]${engine.typ}[/@]
    [@b.col property="version" title="版本"]${engine.version}[/@]
    [@b.col property="profile.name" title="配置"]${(engine.profile.name)!'global'}[/@]
    [@b.col property="jspSupport" title="JSP"]${engine.jspSupport?string('支持','不支持')}[/@]
    [@b.col property="websocketSupport" title="websocket"]${engine.websocketSupport?string('支持','不支持')}[/@]
  [/@]
[/@]
[@b.foot/]
