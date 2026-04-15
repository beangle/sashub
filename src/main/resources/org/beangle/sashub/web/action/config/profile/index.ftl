[#ftl]
[@b.head/]
[#include "../profile_nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="profileSearchForm" action="!search" target="profilelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="profile.name;名称"/]
      [@b.textfields names="profile.title;描述"/]
      <input type="hidden" name="orderBy" value="profile.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="profilelist" href="!search?orderBy=profile.org.id,profile.name"/]
 </div>
</div>
[@b.foot/]
