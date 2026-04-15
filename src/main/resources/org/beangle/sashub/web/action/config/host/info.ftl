[@b.head/]
[@b.toolbar title="主机详情"]bar.addBack();[/@]
<div class="container-md">
  <div class="card card-info card-outline">
    <div class="card-header">
     <i class="fa-solid fa-server"></i>&nbsp;主机信息</span>
        <div class="card-tools">
           <a href="${b.base}/api/${host.profile.qualifiedName}/script/${host.ip}.sh" target="_blank" class="float-right">下载初始化脚本</a>
         </div>
    </div>
    <div class="card-body" style="padding-top: 5px;">
     <table class="infoTable">
      <tr>
        <td class="title" width="20%">名称:</td>
        <td class="content">${host.name}</td>
        <td class="title" width="20%">IP地址:</td>
        <td class="content">${host.ip}</td>
      </tr>
      <tr>
        <td class="title">操作系统:</td>
        <td class="content"><i class="${host.platform.fonticon}"></i>${host.platform.name}(${host.platformVersion})</td>
        <td class="title">内存:</td>
        <td class="content">
          ${host.memory}GB
          [#assign memNeeded=0/]
          [#list servers as s]
            [#if s.maxHeapSize>0] [#assign memNeeded=memNeeded+s.maxHeapSize/] [#else] [#assign memNeeded=memNeeded+s.farm.maxHeapSize/] [/#if]
          [/#list]
          <span [#if host.memory*1024*0.8 < memNeeded]style="color:red"[/#if]>(运行需要${memNeeded}MB)</span>
        </td>
      </tr>
      <tr>
        <td class="title">CPU规格:</td>
        <td class="content">${host.cpu}</td>
        <td class="title">CPU核心数:</td>
        <td class="content">${host.cores}</td>
      </tr>
      <tr>
        <td class="title">系统特性:</td>
        <td class="content" colspan="3">[#list host.features?sort_by('name') as feature]<image style="max-height:20px" title="${feature.name}" src="${feature.logo}"/>[#sep]&nbsp;[/#list]</td>
      </tr>
      <tr>
        <td class="title">开放端口:</td>
        <td class="content" colspan="3">[#list servers?sort_by('httpPort') as server]${server.qualifiedName}(${server.httpPort})[#sep]&nbsp;[/#list]</td>
      </tr>
      <tr>
        <td class="title">部署应用:</td>
        <td class="content" colspan="3">[#list webapps?sort_by(["artifact","name"]) as webapp]${webapp_index+1}. ${webapp.artifact.title}(${webapp.artifact.gav(webapp.version)})[#sep]<br>[/#list]</td>
      </tr>
     </table>
    </div>
  </div>
</div>

[@b.foot/]
