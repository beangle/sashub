[#ftl]
[@b.head/]
[@b.grid items=groups var="group"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称"  width="15%"/]
    [@b.col title="针对配置"]
      [#list group.profiles as p]${p.qualifiedName}[#sep],[/#list]
    [/@]
    [@b.col property="remark" title="说明" width="10%"/]
  [/@]
[/@]
[@b.foot/]
