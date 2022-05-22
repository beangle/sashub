[#ftl]
[@b.head/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="webappSearchForm" action="!search" target="webapplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="webapp.artifact.artifactId;名称"/]
      [@b.textfields names="webapp.version;版本"/]
      [@b.select name="webapp.profile.id" items=profiles label="配置" value=profile! option="id,qualifiedName"/]
      <input type="hidden" name="orderBy" value="webapp.artifact.groupId"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="webapplist" href="!search?orderBy=webapp.artifact.groupId&webapp.profile.id="+(profile.id)!/]
 </div>
</div>
[@b.foot/]
