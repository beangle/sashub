[@b.toolbar title="特性列表"]
  bar.addItem("添加特性","addFeature()");
[/@]
[@b.div class="container-fluid" class="d-flex justify-content-around" style="flex-wrap:wrap;"]
[#list platformFeatures as platformFeature]
    <div class="card card-primary card-outline" style="width:350px">
      <div class="card-body">
        <div style="max-height: 80px;max-width: 80px;float:left">
          <image style="max-height: 80px;max-width: 80px;" src="${platformFeature.logo!}"/>
        </div>
        <div style="display:inline-block">[@b.a href="!info?id="+platformFeature.id]${platformFeature.name}[/@]</div>
        <div style="display:inline-block">${platformFeature.version!}</div>
        <div class="text-muted" style="height:4.3em;overflow:hidden">${platformFeature.description!}</div>
        <div>
          <div style="float:left;color:gray;">依赖:[#list platformFeature.dependencies?sort_by('name') as feature]<image style="max-height:15px" src="${feature.logo}" title="${feature.name}"/>[#sep]&nbsp;[/#list]</div>
          <div style="float:right;color:gray;">[#list platformFeature.scripts as script]<i class="${script.platform.fonticon}" title="${script.platform.name}"></i>[#sep]&nbsp;[/#list]</div>
        </div>
      </div>
    </div>
[/#list]
[/@]
  [@b.form name="featureForm" action="!editNew"]
    <input name="featureId" value="" type="hidden"/>
    <input name="scriptId" value="" type="hidden"/>
  [/@]
<script>
  function addFeature(featureId){
    var form=document.featureForm;
    bg.form.submit(form,'${b.url("!editNew")}')
  }
  function editScript(featureId,scriptId){
    var form=document.featureForm;
    form['featureId'].value=featureId;
    form['scriptId'].value=scriptId;
    bg.form.submit(form,'${b.url("!editScript")}')
  }

  function removeScript(featureId,scriptId){
    var form=document.featureForm;
    form['featureId'].value=featureId;
    form['scriptId'].value=scriptId;
    bg.form.submit(form,'${b.url("!removeScript")}')
  }
</script>
