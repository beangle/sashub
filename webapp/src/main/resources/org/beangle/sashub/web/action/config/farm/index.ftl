[#ftl]
[@b.head/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="farmSearchForm" action="!search" target="farmlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="farm.name;名称"/]
      [@b.textfields names="farm.ip;IP"/]
      [@b.select name="farm.profile.id" items=profiles label="配置" value=profile! option="id,qualifiedTitle"/]
      <input type="hidden" name="orderBy" value="farm.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="farmlist" href="!search?orderBy=farm.name&farm.profile.id="+(profile.id)!/]
 </div>
</div>
[@b.foot/]
