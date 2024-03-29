[#ftl]
[@b.head/]
<script type="text/javascript">
  beangle.load(["bui-tabletree"]);
</script>
[#include "../status.ftl"/]
[@b.grid  items=menus var="menu" sortable="false"]
[@b.gridbar title="菜单列表"]
  action.addParam('menu.app.id',"${Parameters['menu.app.id']!}");
  function activate(isActivate){
    return action.multi("activate","确定操作?","isActivate="+isActivate);
  }
  bar.addItem("${b.text("action.new")}",action.add());
  bar.addItem("${b.text("action.edit")}",action.edit());
  bar.addItem("${b.text("action.delete")}",action.remove());
  bar.addItem("${b.text("action.export")}",action.method("exportToXml",null,null,'_blank'));
  bar.addItem("导入",action.method('importFromXml'));
[/@]
  [@b.row]
    <tr [#if menu??] title="入口及备注:${(menu.entry.name)!} ${(menu.remark?html)!}" id="${menu.indexno}"[/#if]>
    [@b.boxcol/]
    [@b.treecol title="common.name" width="30%"][@b.a href="!info?id=${menu.id}"]${menu.indexno}  [#if menu.fonticon??] <i class="${menu.fonticon}"></i> [/#if]${menu.name}[/@][/@]
    [@b.col property="enName" title="英文名称" width="15%"/]
    [@b.col title="使用资源"][#list menu.resources as re]${re.title?html}[#if re_has_next],[/#if][/#list][/@]
    [@b.col property="enabled" width="10%" title="common.status"][@enableInfo menu.enabled/][/@]
    </tr>
  [/@]
[/@]
[@b.foot/]
