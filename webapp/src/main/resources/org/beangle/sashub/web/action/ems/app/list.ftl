[#ftl]
[@b.head/]

[#macro enableInfo enabled]
[#if enabled]<img height="15px" width="15px" src="${b.static_url("bui","icons/16x16/actions/activate.png")}"/>${b.text("action.activate")}[#else]<font color="red"><img height="15px" width="15px" src="${b.static_url("bui","icons/16x16/actions/freeze.png")}"/>${b.text("action.freeze")}</font>[/#if]
[/#macro]

[@b.grid items=apps var="app"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="20%" property="name" title="名称"/]
    [@b.col width="18%" property="title" title="标题"/]
    [@b.col width="10%" property="navStyle" title="导航风格"/]
    [@b.col width="20%" property="base" title="上下文"/]
    [@b.col width="10%" title="菜单"]
       [@b.a href="menu!exportToXml?menu.app.id="+app.id target='_blank']导出[/@]
    [/@]
    [@b.col width="18%" title="依赖项" property="services"/]
  [/@]
[/@]
[@b.foot/]
