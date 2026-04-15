[#ftl]
[@b.head/]
[#include "nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="platformSearchForm" action="!search" target="platformlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="platform.name;名称"/]
      [@b.textfields names="platform.majorVersion;主版本"/]
      <input type="hidden" name="orderBy" value="platform.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="platformlist" href="!search?orderBy=platform.name"/]
 </div>
</div>
[@b.foot/]
