global
    log         127.0.0.1 local2
    chroot      /var/lib/haproxy
    pidfile     /var/run/hapid
    maxconn     ${profile.maxconn}
    user        haproxy
    group       haproxy
    daemon
    tune.ssl.default-dh-param 2048
    ssl-default-bind-options no-sslv3 no-tlsv10
    ssl-default-bind-ciphers PROFILE=SYSTEM
    ssl-default-server-ciphers PROFILE=SYSTEM
    stats socket /var/lib/haproxy/stats

defaults
    mode                    http
    log                     global
    option                  httplog
    option                  dontlognull
    option http-server-close
    option forwardfor       except 127.0.0.0/8
    option                  redispatch
    retries                 3
    timeout http-request    2m
    timeout queue           1m
    timeout connect         30s
    timeout client          2m
    timeout server          10m
    timeout http-keep-alive 60s
    timeout check           60s
    maxconn                 ${profile.maxconn}

    stats refresh 30s
    stats uri /status
    stats realm haproxy-user
    stats auth admin:ChangeItNow

frontend main
    bind *:${profile.httpPort}
    [#if profile.enableHttps]
    bind *:${profile.httpsPort} ssl crt /etc/haproxy/${profile.hostname}.pem no-sslv3 no-tlsv10
    http-request set-header X-Forwarded-Proto https if { ssl_fc }
    http-request set-header X-Forwarded-Port %[dst_port]
    [#if profile.forceHttps && profile.hostname??]
    acl is_me hdr_beg(host) ${profile.hostname}
    redirect scheme https if !{ ssl_fc } is_me
    [/#if]
    [/#if]

[#assign max_path_len=1]
[#assign max_proxy_len=1]

[#list webapps as webapp]
  [#if webapp.contextPath?length > max_path_len]
    [#assign max_path_len=webapp.contextPath?length]
  [/#if]
  [#if entryPoints.get(webapp).name?length > max_proxy_len]
    [#assign max_proxy_len=entryPoints.get(webapp).name?length]
  [/#if]
[/#list]

[#list webapps as webapp]
    [#if webapp.contextPath?length>1]
    acl is${webapp.contextPath?replace('/','_')?right_pad(max_path_len)} path_beg ${webapp.contextPath}
    [/#if]
[/#list]

[#assign use_backends=[]]
[#list webapps as webapp]
[#if webapp.contextPath?length>1]
[#assign this_backend]    use_backend ${entryPoints.get(webapp).name?right_pad(max_proxy_len)}  if is${webapp.contextPath?replace('/','_')}[/#assign]
[#assign use_backends=use_backends+[this_backend]/]
[#else]
  [#assign default_server=entryPoints.get(webapp).name/]
[/#if]
[/#list]

[#list use_backends as use_backend]
${use_backend}
[/#list]

[#if default_server??]
    default_backend ${default_server}
[/#if]

    [#list backends?sort_by('name') as backend]
backend ${backend.name}
    [#if backend.servers?size =1]
    [#if backend.options??]
${addMargin(backend.options)}
    [/#if]
    [#else]
${addMargin(backend.options!"balance roundrobin")}
    [/#if]
[#t]
    [#list backend.servers as server]
    [#assign port=server.httpPort][#if server.proxyHttpPort??][#assign port=server.proxyHttpPort][/#if]
    server ${server.host.ip?replace('.','_')?replace(':','_')}_${port} ${server.host.ip}:${port} ${server.proxyOptions!'check'}
    [/#list]

    [/#list]

[#function addMargin(text)]
  [#assign lines=text?split('\n')]
  [#assign res =""/]
  [#list lines as line]
  [#assign res = res + ("    " + line?trim)]
  [#if line_has_next]
  [#assign res = res + "\n"]
  [/#if]
  [/#list]
  [#return res/]
[/#function]
