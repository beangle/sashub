[#ftl]
[@b.head/]
[@b.grid items=hosts var="host"]
  [@b.gridbar]
    [#if (Parameters['host.profile.id']!'')?length>0]
    bar.addItem("${b.text("action.new")}",action.add());
    [/#if]
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="name" title="名称" width="10%"]
      [@b.a href="!info?id=${host.id}"]
       <image src="${host.profile.org.logoUrl}" style="width:30px"/>
       ${host.name!}
      [/@]
    [/@]
    [@b.col property="ip" title="IP地址" width="13%"]${host.ip}[/@]
    [@b.col property="platform.name" title="操作系统" width="12%"]<i class="${host.platform.fonticon}"></i>${host.platform.name}(${host.platformVersion})[/@]
    [@b.col title="系统特性"][#list host.features?sort_by('name') as feature] [#if feature.support(host.platform)]<image style="max-height:20px" src="${feature.logo}" title="${feature.name}"/>[/#if][#sep]&nbsp;[/#list][/@]
    [@b.col property="memory" title="内存" width="8%"]${host.memory}GB[/@]
    [@b.col property="arch" title="平台" width="10%"/]
    [@b.col property="cores" title="CPU核心" width="8%"]${host.cores}[/@]
    [@b.col property="cpu" title="CPU规格" width="20%"]<span style="font-size:0.8em">${host.cpu}</span>[/@]
  [/@]
[/@]
[@b.foot/]
