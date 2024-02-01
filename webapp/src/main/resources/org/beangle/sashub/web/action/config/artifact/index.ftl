[#ftl]
[@b.head/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="artifactSearchForm" action="!search" target="artifactlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="artifact.artifactId;名称"/]
      [@b.textfields names="artifact.groupId;分组"/]
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="artifactlist" href="!search?orderBy=artifact.artifactId"/]
 </div>
</div>
[@b.foot/]
