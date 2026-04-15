[#ftl]
[@b.head/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="engineSearchForm" action="!search" target="enginelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="engine.name;名称"/]
      [@b.textfields names="engine.typ;类型"/]
      <input type="hidden" name="orderBy" value="engine.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="enginelist" href="!search?orderBy=engine.name"/]
 </div>
</div>
[@b.foot/]
