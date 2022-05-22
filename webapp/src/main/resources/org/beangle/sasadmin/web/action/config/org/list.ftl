[#ftl]
[@b.head/]
[@b.grid items=orgs var="org"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="10%"/]
    [@b.col property="logoUrl" title="logo" width="15%"]<img src="${org.logoUrl}" style="width:30px">[/@]
    [@b.col property="title" title="标题" width="60%"][@b.a href="!info?id=${org.id}"]${org.title!}[/@][/@]
    [@b.col property="shortTitle" title="简称" width="10%"/]
  [/@]
[/@]
[@b.foot/]
