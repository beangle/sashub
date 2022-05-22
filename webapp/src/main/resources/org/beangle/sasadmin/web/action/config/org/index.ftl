[#ftl]
[@b.head/]
[#include "../profile_nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="orgSearchForm" action="!search" target="orglist" title="ui.searchForm" theme="search"]
      [@b.textfields names="org.name;名称"/]
      [@b.textfields names="org.title;标题"/]
      <input type="hidden" name="orderBy" value="org.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="orglist" href="!search?orderBy=org.name"/]
 </div>
</div>
[@b.foot/]
