[#ftl]
[@b.head/]
[@b.grid items=platforms var="platform"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="50%"/]
    [@b.col property="majorVersion" title="主版本" width="25%"/]
    [@b.col property="fonticon" title="字体图标" width="20%"]
      <i class="${platform.fonticon}"></i>
    [/@]
  [/@]
[/@]
[@b.foot/]
