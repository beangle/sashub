[#ftl]
[@b.head/]
[@b.nav class="nav-tabs nav-tabs-compact"]
  [@b.navitem href="asset"]静态资源[/@]
  [@b.navitem href="asset-group"]静态资源组[/@]
[/@]

<div class="search-container">
 <div class="search-panel">
    [@b.form name="groupSearchForm" action="!search" target="grouplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="group.base;上下文"/]
      [@b.textfields names="group.title;标题"/]
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="grouplist" href="!search?orderBy=group.name"/]
 </div>
</div>
[@b.foot/]
