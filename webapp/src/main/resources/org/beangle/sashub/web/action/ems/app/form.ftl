[#ftl]
[@b.head/]
[@b.toolbar title="修改应用"]bar.addBack();[/@]
<style>form.listform label.title{width:120px;}</style>
[@b.tabs]
  [@b.form action=b.rest.save(app) theme="list"]
    [@b.textfield name="app.name" label="名称" value=app.name! required="true" maxlength="200"/]
    [@b.textfield name="app.title" label="标题" value=app.title! required="true" maxlength="200"/]
    [@b.select name="app.domain.id" items=domains label="group" value=app.domain! option="id,title" required="true" /]
    [@b.textfield label="artifactId" name="app.artifactId" value=app.artifactId! required="true" maxlength="100"/]
    [@b.textfield name="app.base" label="上下文地址" value=app.base! required="true" maxlength="200" style="width:300px"/]
    [@b.textfield name="app.url" label="入口" value=app.url! required="true" maxlength="200" style="width:300px"/]
    [@b.textfield name="app.navStyle" label="导航风格" value=app.navStyle! required="true" /]
    [@b.textfield name="app.services" label="外部依赖" value=app.services! required="false" /]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]
