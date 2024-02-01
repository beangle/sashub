[#ftl]
[@b.head/]
[#include "../platform/nav.ftl"/]
<div class="search-container">
 <div class="search-panel">
    [@b.form name="platformFeatureSearchForm" action="!search" target="platformFeaturelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="platformFeature.name;名称"/]
      <input type="hidden" name="orderBy" value="platformFeature.name"/>
    [/@]
 </div>
 <div class="search-list">
   [@b.div id="platformFeaturelist" href="!search?orderBy=platformFeature.name"/]
 </div>
</div>
[@b.foot/]
