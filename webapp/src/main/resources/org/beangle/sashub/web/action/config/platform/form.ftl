[@b.head/]
[@b.toolbar title="修改或新增平台"]bar.addBack();[/@]

[@b.form action=b.rest.save(platform) theme="list"]
  [@b.textfield label="名称" name="platform.name" value=platform.name! required="true"  /]
  [@b.textfield label="主版本" name="platform.majorVersion" value=platform.majorVersion! required="true"   /]
  [@b.textfield label="字体图标" name="platform.fonticon" value=platform.fonticon! required="true"/]
  [@b.formfoot]
    [@b.reset/] &nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]
[@b.foot/]