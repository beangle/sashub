[@b.toolbar title="特性脚本列表"]
  bar.addBack();
[/@]
[@b.messages slash="3"/]
 [@b.card class="card-info card-outline"]
   [@b.card_header]
     <div>
       <image style="max-height: 30px;max-width: 30px;" src="${platformFeature.logo!}"/>&nbsp;${platformFeature.name} ${platformFeature.version}
       ${platformFeature.description}
     </div>
     <div style="display:block">
     [@b.a href="!remove?id="+platformFeature.id class="btn btn-sm btn-warning" ]<i class="fa fa-xmark"></i>删除[/@]
     [@b.a href="!edit?id="+platformFeature.id class="btn btn-sm btn-primary"]<i class="fa fa-edit"></i>修改[/@]
     [@b.a onclick="addScript('${platformFeature.id}');return false;" class="btn btn-sm btn-success"]<i class="fa fa-plus"></i>增加脚本[/@]
     </div>
   [/@]
   <table class="table">
    <tbody>
   [#list platformFeature.scripts as script]
     <tr>
       <td style="width: 10%;"><div><i class="${script.platform.fonticon}"></i> ${script.platform.name}</div>
           <div>
              [@b.a onclick="removeScript('${platformFeature.id}','${script.id}');return false;" class="btn btn-sm btn-warning"]<i class="fa fa-xmark"></i>删除[/@]
              [@b.a onclick="editScript('${platformFeature.id}','${script.id}');return false;" class="btn btn-sm btn-primary"]<i class="fa fa-edit"></i>修改[/@]
           </div>
       </td>
       <td style="border-left: 1px solid #dee2e6;">
<pre><code>${script.scripts}</code></pre>
       </td>
     </tr>
   [/#list]
    </tbody>
   </table>
 [/@]
  [@b.form name="featureForm" action="!editNew"]
    <input name="featureId" value="" type="hidden"/>
    <input name="scriptId" value="" type="hidden"/>
  [/@]
<script>
  function addScript(featureId){
    var form=document.featureForm;
    form['featureId'].value=featureId;
    bg.form.submit(form,'${b.url("!addScript")}')
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
