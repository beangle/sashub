[#ftl]
[@b.head/]
[@b.grid items=assets var="asset"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="20%"][@b.a href="!info?id=${asset.id}" ]${asset.name}[/@][/@]
    [@b.col property="base" title="上下文"  width="15%"/]
    [@b.col property="group.name" title="所在组"  width="15%"/]
    [@b.col property="remark" title="说明"  width="15%"/]
    [@b.col title="组件" ][#list asset.bundles?sort_by("uri") as b]${b.uri}[#sep]&nbsp;[/#list][/@]
  [/@]
[/@]
[@b.foot/]
