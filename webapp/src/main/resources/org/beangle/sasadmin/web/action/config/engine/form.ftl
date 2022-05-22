[@b.head/]
[@b.toolbar title="修改或新增引擎"]bar.addBack();[/@]

[#assign listeners]
[#list engine.listeners as l]
${l}
[/#list]
[/#assign]

[#assign jars]
[#list engine.jars as l]
${l}
[/#list]
[/#assign]

[#assign typeNames={}/]
[#list types as t][#assign typeNames=typeNames+{t:t}/][/#list]

[@b.form action=b.rest.save(engine)theme="list"]
  [@b.textfield label="common.name" name="engine.name" value="${engine.name!}" style="width:200px;"  required="true" maxlength="100" /]
  [@b.select label="类型" name="engine.typ" value=engine.typ items=typeNames required="true" /]
  [@b.textfield label="版本" name="engine.version" value=engine.version! style="width:100px;" required="true" maxlength="50"/]
  [@b.radios label="JSP" name="engine.jspSupport"  value= engine.jspSupport items="1:支持,0:不支持" /]
  [@b.radios label="websocket" name="engine.websocketSupport" value=engine.websocketSupport items="1:支持,0:不支持"/]
  [@b.textarea name="listeners" label="监听器" value=listeners  maxlength="500" cols="60"/]
  [@b.textarea name="jars" label="Jars" value=jars maxlength="500" cols="60"/]
  [@b.select label="针对配置" name="engine.profile.id" value=engine.profile! items=profiles empty="全局"/]
  [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]
[@b.foot/]
