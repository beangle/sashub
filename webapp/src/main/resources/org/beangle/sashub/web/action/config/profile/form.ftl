[@b.head/]
[@b.toolbar title="修改或新增配置"]bar.addBack();[/@]

[@b.form action=b.rest.save(profile) theme="list"]
  [@b.textfield label="common.name" name="profile.name" value="${profile.name!}"  required="true" maxlength="100" /]
  [@b.textfield label="标题" name="profile.title" value="${profile.title!}" required="true" maxlength="50"/]
  [@b.select name="profile.org.id" items=orgs label="组织" value=profile.org! option="id,title"/]
  [@b.textfield label="版本" name="profile.sasVersion" value=profile.sasVersion!  required="true" maxlength="50"/]
  [@b.textfield label="IP" name="profile.ip" value=profile.ip! required="true" maxlength="50"/]
  [@b.textfield label="域名" name="profile.hostname" value=profile.hostname! maxlength="50"/]
  [@b.radios label="代理类型" name="profile.proxyEngine" value=profile.proxyEngine!'haproxy' items="haproxy:Haproxy,nginx:Nginx" /]
  [@b.number label="http端口" name="profile.httpPort" value=profile.httpPort! maxlength="5" max="90000"/]
  [@b.number label="最大连接数" name="profile.maxconn" value=profile.maxconn! maxlength="6" max="50000"/]

  [@b.radios label="启用Https" name="profile.enableHttps" value=profile.enableHttps /]
  [@b.radios label="强制Https" name="profile.forceHttps" value=profile.forceHttps /]
  [@b.number label="https端口" name="profile.httpsPort" value=profile.httpsPort maxlength="5" max="90000"/]
  [@b.textfield label="SSL加密套件" name="profile.sslCiphers" value=profile.sslCiphers! maxlength="100"/]
  [@b.textfield label="SSL协议" name="profile.sslProtocols" value=profile.sslProtocols! maxlength="100" /]

  [@b.textfield label="本地仓库" name="profile.localRepo" value=profile.localRepo required="true" maxlength="100" comment="默认为~/.m2/repository"/]
  [@b.textfield label="远程仓库" name="profile.remoteRepo" value=profile.remoteRepo style="width:400px;" required="true" maxlength="300"/]

  [@b.field label="样例仓库"]
    <div style="margin-left:100px">
    <span style="font-size:0.8rem;color: #999;">Maven官方:</span>https://repo1.maven.org/maven2<br>
    <span style="font-size:0.8rem;color: #999;">阿里云:</span>https://maven.aliyun.com/nexus/content/groups/public<br>
    <span style="font-size:0.8rem;color: #999;">华为云:</span>https://repo.huaweicloud.com/repository/maven/
    </div>
  [/@]
  [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
[/@]
[@b.foot/]
