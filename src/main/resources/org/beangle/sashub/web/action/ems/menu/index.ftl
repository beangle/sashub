[#ftl]
[@b.head/]
[#include "../menu-nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form action="!search?orderBy=menu.indexno" title="ui.searchForm" target="menulist" theme="search"]
      [@b.select name="menu.app.id" value=current_app items=apps?sort_by('qualifiedTitle') label="应用" option="id,qualifiedTitle"/]
      [@b.textfields names="menu.indexno;顺序号,menu.name;名称,menu.enName;英文名,menu.entry.name;menu.entry"/]
      [@b.select name="menu.enabled" items=profiles label="common.status" items={'true':'${b.text("action.activate")}','false':'${b.text("action.freeze")}'}  empty="..."/]
    [/@]
 </div>
 <div class="search-list">
    [@b.div  href="!search?menu.app.id=${current_app.id}&orderBy=menu.indexno" id="menulist"/]
 </div>
</div>
[@b.foot/]
