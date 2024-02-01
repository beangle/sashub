[#ftl]
[@b.head/]
[@b.nav class="nav-tabs nav-tabs-compact"]
  [@b.navitem href="asset"]静态资源[/@]
  [@b.navitem href="asset-group"]静态资源组[/@]
[/@]

<div class="search-container">
 <div class="search-panel">
    [@b.form name="assetSearchForm" action="!search" target="assetlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="asset.name;名称"/]
      [@b.textfields names="asset.base;上下文"/]
      [@b.select label="所在组" name="asset.group.id" items=groups/]
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="assetlist" href="!search?orderBy=asset.base"/]
 </div>
</div>
[@b.foot/]
