[@b.head/]
[@b.toolbar title="修改或新增服务"]bar.addBack();[/@]

[@b.form action=b.rest.save(server)theme="list"]
  [@b.field label="配置"]${farm.name} ${farm.profile.title}[/@]
  [@b.textfield label="common.name" name="server.name" value=server.name! required="true" maxlength="100" /]
  [@b.select label="主机" name="server.host.id" items=hosts required="true" value=server.host!/]
  [@b.number label="http端口" name="server.httpPort" value=server.httpPort! maxlength="10" min="81" max="99999"/]
  [@b.number label="堆上限" name="server.maxHeapSize" value=server.maxHeapSize!  max="100000" required="true" maxlength="10" comment="MB,0表示和集群一致"/]
  [@b.radios label="访问日志" name="server.enableAccessLog" value=server.enableAccessLog items="1:启用,0:忽略"/]
  [@b.textfield label="http代理端口" name="server.proxyHttpPort" value=server.proxyHttpPort! maxlength="10"/]
  [@b.textarea label="代理配置" name="server.proxyOptions" value=server.proxyOptions! maxlength="300" cols="80" style="width:500px;" /]
  [@b.formfoot]
    <input type="hidden" value="${farm.id}" name="server.farm.id"/>
    [@b.reset/] &nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]

[@b.foot/]
