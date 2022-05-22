[#ftl]
[@b.head/]
[@b.grid items=webapps var="webapp"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("更新版本",action.multi('upgrade',"是否更新到该组件的最新版?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col property="artifact.title" title="标题" width="15%"/]
    [@b.col property="artifact.artifactId" title="名称" width="25%"]
      [@b.a href="!info?id=${webapp.id}"]${webapp.artifact.artifactId!}[/@]
    [/@]
    [@b.col property="version" title="版本" width="12%"]
      [#if webapp.version != webapp.artifact.latestVersion]<span title="可升级至${webapp.artifact.latestVersion}">${webapp.version}<i class="fa-solid fa-circle-up"></i></span>[#else]${webapp.version}[/#if]
    [/@]
    [@b.col property="contextPath" title="上下文" width="18%"/]
    [@b.col title="运行在" width="27%"]
       [#list webapp.runAt as s]${s.farm.name}.${s.name}<span style="font-size:0.8rem;color: #999;">${s.host.name}:${s.httpPort}</span>[#sep]<br>[/#list]
    [/@]
    [@b.col property="unpack" title="解压执行" width="8%"/]
  [/@]
[/@]
[@b.foot/]