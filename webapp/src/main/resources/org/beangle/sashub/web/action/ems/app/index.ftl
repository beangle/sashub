[#ftl]
[@b.head/]
[#include "../app-nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="appSearchForm" action="!search" target="applist" title="ui.searchForm" theme="search"]
      [@b.select name="app.group.id" items=groups label="所在分组" empty="..." option="id,title"/]
      [@b.textfields names="app.name;名称,app.title;标题"/]
      <input type="hidden" name="orderBy" value="app.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="applist" href="!search?orderBy=name"/]
 </div>
</div>
[@b.foot/]
