[#ftl]
[@b.head/]
[#include "../app-nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="appGroupSearchForm" action="!search" target="appGrouplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="appGroup.name;名称,appGroup.title;标题"/]
      <input type="hidden" name="orderBy" value="appGroup.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="appGrouplist" href="!search?orderBy=name"/]
 </div>
</div>
[@b.foot/]
