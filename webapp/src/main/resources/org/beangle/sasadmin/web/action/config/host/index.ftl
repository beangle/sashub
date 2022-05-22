[#ftl]
[@b.head/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="hostSearchForm" action="!search" target="hostlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="host.name;名称"/]
      [@b.textfields names="host.ip;IP"/]
      [@b.select name="host.profile.id" items=profiles label="配置" value=profile! option="id,qualifiedName"/]
      <input type="hidden" name="orderBy" value="host.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="hostlist" href="!search?orderBy=host.name&host.profile.id="+(profile.id)!/]
 </div>
</div>
[@b.foot/]
