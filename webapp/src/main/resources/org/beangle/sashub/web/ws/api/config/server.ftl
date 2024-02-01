[#ftl attributes={'content_type':'application/xml'}]
<Sas version="${profile.sasVersion}">
  <Repository remote="${profile.remoteRepo}"/>
  <Engines>
    [#list engines as engine]
    <Engine name="${engine.name}" type="${engine.typ}" version="${engine.version}"[#if engine.javaEngine] jspSupport="${engine.jspSupport?c}" websocketSupport="${engine.websocketSupport?c}"[/#if]>
      [#list engine.listeners as listener]
      <Listener className="${listener}"/>
      [/#list]
      [#list engine.jars as jar]
      <Jar uri="${jar}"/>
      [/#list]
    </Engine>
    [/#list]
  </Engines>

  <Hosts>
  [#list hosts as host]
    <Host name="${host.name}" ip="${host.ip}"/>
  [/#list]
  </Hosts>

  <Farms>
    [#list farms as farm]
    <Farm name="${farm.name}" engine="${farm.engine.name}" [#if farm.enableAccessLog]enableAccessLog="true"[/#if] maxHeapSize="${farm.maxHeapSize}M">
      [#if farm.serverOptions??]
      <ServerOptions>${farm.serverOptions}</ServerOptions>
      [/#if]
      [#if farm.proxyOptions??]
      <ProxyOptions>
      <![CDATA[${farm.proxyOptions}]]>
      </ProxyOptions>
      [/#if]
      <Http maxThreads="${farm.http.maxThreads}"/>
      [#list farm.servers as server]
      <Server name="${server.name}" host="${server.host.name}" http="${server.httpPort}"[#rt]
         [#if server.maxHeapSize>0] maxHeapSize="${server.maxHeapSize}M"[/#if][#t]
         [#if server.enableAccessLog] enableAccessLog="true"[/#if][#t]
         [#if server.proxyHttpPort??] proxyHttpPort="${server.proxyHttpPort}"[/#if][#t]
         [#if server.proxyOptions??] proxyOptions="${server.proxyOptions}"[/#if][#t]
         />[#lt]
      [/#list]
    </Farm>
    [/#list]
  </Farms>
  <Webapps>
    [#list webapps as webapp]
    <Webapp uri="gav://${webapp.artifact.gav(webapp.version)}" path="${webapp.contextPath}" [#rt]
     runAt="[#list webapp.targets as s]${s.farm.name}.${s.name}[#sep],[/#list]" [#t]
     [#if webapp.unpack] unpack="true"[/#if][#t]
     [#if !webapp.artifact.resolveSupport] resolveSupport="false"[/#if] /> [#lt]
    [/#list]
  </Webapps>

  <Proxy engine="${profile.proxyEngine}" [#if profile.hostname??]hostname="${profile.hostname}"[/#if] maxconn="${profile.maxconn}" [#if profile.httpPort!=80] httpPort="${profile.httpPort}"[/#if]>
    [#if profile.enableHttps]
    <Https port="${profile.httpsPort}" [#if profile.sslCiphers??]ciphers="${profile.sslCiphers}"[/#if] [#if profile.sslProtocols??]protocols="${profile.sslProtocols}"[/#if] forceHttps="${profile.forceHttps?c}" />
    [/#if]
    <Status url="/status" auth="admin:ChangeItNow"/>
  </Proxy>
</Sas>
