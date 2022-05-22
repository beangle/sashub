[@b.head/]
[@b.toolbar title="组件详情"]bar.addBack();[/@]
[#assign packagings={'jar':'Jar','war':'War','bin':'Bin'}/]
<div class="container-md">
  <div class="card card-info card-outline">
    <div class="card-header">
     <i class="fa-solid fa-cubes"></i>&nbsp;组件信息</span>
    </div>
    <div class="card-body" style="padding-top: 5px;">
     <table class="infoTable">
      <tr>
        <td class="title" width="20%">标题:</td>
        <td class="content">${(artifact.title)!}</td>
        <td class="title" width="20%">版本号:</td>
        <td class="content">${artifact.latestVersion!}</td>
      </tr>
      <tr>
        <td class="title">groupId:</td>
        <td class="content">${artifact.groupId}</td>
        <td class="title">artifactId:</td>
        <td class="content">${artifact.artifactId}</td>
      </tr>
      <tr>
        <td class="title">classifier:</td>
        <td class="content">${artifact.classifier!}</td>
        <td class="title">packaging:</td>
        <td class="content">${artifact.packaging}</td>
      </tr>
      <tr>
        <td class="title" >解析支持:</td>
        <td class="content">${(artifact.resolveSupport)?c}</td>
        <td class="title" >其他:</td>
        <td class="content">[#if artifact.jspSupport]jsp[/#if]&nbsp;[#if artifact.websocketSupport]websocket[/#if]</td>
      </tr>
      <tr>
        <td class="title" >详情介绍:</td>
        <td class="content" colspan="3">${(artifact.description)!}</td>
      </tr>
      <tr>
        <td class="title" >部署情况:</td>
        <td class="content" colspan="3">
        [#list webapps as webapp]
          ${webapp.profile.title} ${webapp.version} ${webapp.contextPath}[#sep]<br>
        [/#list]
        </td>
      </tr>
     </table>
    </div>
  </div>
</div>

[@b.foot/]
