[@b.head/]
[@b.toolbar title="修改或新增webapp"]bar.addBack();[/@]

[@b.form action=b.rest.save(webapp)theme="list"]
  [@b.field label="配置"]${profile.name} ${profile.title}[/@]
  [@b.select label="组件" name="webapp.artifact.id" items=artifacts required="true" value=webapp.artifact!
             option=r"${item.title} ${item.artifactId} ${item.latestVersion}" style="width:400px" chosenMin="4" onchange="updateDefaults(this)"/]
  [@b.textfield label="版本" name="webapp.version" value=webapp.version! required="true" maxlength="100" /]
  [@b.textfield label="上下文" name="webapp.contextPath" value=webapp.contextPath! required="true" maxlength="100"/]
  [@b.radios label="解压执行" name="webapp.unpack" value=webapp.unpack!/]
  [@b.select label="运行在" name="server.id" items=servers required="true" values=webapp.targets! multiple="true"
             option=r"${item.farm.name}.${item.name}(${item.host.name}:${item.httpPort})" chosenMin="1" style="width:500px"/]
  [@b.formfoot]
    <input type="hidden" value="${profile.id}" name="webapp.profile.id"/>
    [@b.reset/] &nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]
<script>
   var artifacts={}
   [#list artifacts as artifact]
     artifacts['${artifact.id}']={'contextPath':'${artifact.contextPath}','version':'${artifact.latestVersion}'}
   [/#list]
   function updateDefaults(ele){
      var form = ele.form;
      if(ele.value){
        var artifact=artifacts[ele.value];
        form['webapp.version'].value=artifact.version;
        form['webapp.contextPath'].value=artifact.contextPath;
      }
   }
</script>
[#list 1..10 as i]<br>[/#list]
[@b.foot/]
