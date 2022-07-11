[@b.head/]
[@b.toolbar title="修改或新增脚本"]bar.addBack();[/@]

[@b.form action="!saveScript" theme="list"]
  [@b.select name="script.platform.id" items=platforms required="true" label="平台" value=script.plaform! /]
  [@b.textarea label="脚本" name="script.scripts" value=script.scripts! required="true" maxlength="4000" cols="100" rows="20"/]
  [@b.formfoot]
    <input type="hidden" name="script.id" value="${script.id!}"/>
    <input type="hidden" name="script.feature.id" value="${script.feature.id}"/>
    [@b.reset/] &nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]
[@b.foot/]
