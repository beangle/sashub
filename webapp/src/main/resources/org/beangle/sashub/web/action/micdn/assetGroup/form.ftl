[@b.head/]
[@b.toolbar title="修改或新增静态组"]bar.addBack();[/@]
  [@b.form action=b.rest.save(group) theme="list"]
    [@b.textfield label="名称" name="group.name" value=group.name required="true" maxlength="100" /]
    [@b.textfield label="描述" name="group.remark" value=group.remark maxlength="100" /]
    [@b.select label="针对配置" name="group.profile.id" value=group.profile! items=profiles empty="..." option="id,qualifiedName"/]
    [@b.formfoot]
      [@b.reset/] &nbsp;&nbsp;[@b.submit value="action.submit" /]
    [/@]
  [/@]
[@b.foot/]
