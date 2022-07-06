user nginx;
worker_processes auto;
worker_rlimit_nofile 65535;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

include /usr/share/nginx/modules/*.conf;

events {
    worker_connections ${profile.maxconn};
    multi_accept on;
    use epoll;
}

http {
    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;
    types_hash_bucket_size 1024;

    include             /etc/nginx/mime.types;
    default_type        application/octet-stream;

[#if profile.enableHttps??]
    ssl_session_cache   shared:SSL:10m;
    ssl_session_timeout 10m;
[/#if]
    include /etc/nginx/conf.d/*.conf;

    [#list backends?sort_by("name") as backend]
    upstream ${backend.name}{
    [#if backend.options??]${addMargin(backend.options)}[/#if][#t]

    [#list backend.servers as server]
        [#assign port=server.httpPort][#if server.proxyHttpPort??][#assign port=server.proxyHttpPort][/#if]
        server ${server.host.ip}:${port} ${server.options!};
    [/#list]
    }
    [/#list]

    [#if profile.enableHttps?? && profile.forceHttps][#--跳转到https--]
    server {
        listen ${profile.httpPort} default_server;
        listen [::]:${profile.httpPort} default_server;
        [#if profile.hostname??]
        server_name  ${profile.hostname};
        [/#if]
        return 301 https://$server_name$request_uri;
    }
    [/#if]

    server {
        [#if !(profile.enableHttps && profile.forceHttps)]
        listen ${profile.httpPort};
        [/#if]
        [#if profile.enableHttps??]
        listen ${profile.httpsPort} ssl;
        [/#if]
        [#if profile.hostname??]
        server_name  ${profile.hostname};
        [/#if]
        [#if profile.enableHttps && profile.hostname??]
        ssl_certificate /etc/nginx/${profile.hostname}.crt;
        ssl_certificate_key /etc/nginx/${profile.hostname}.key;
        ssl_protocols      TLSv1 TLSv1.1 TLSv1.2;
        ssl_ciphers       HIGH:!aNULL:!MD5;
        keepalive_timeout   70;
        [/#if]
        root         /usr/share/nginx/html;
        access_log off;
        proxy_set_header  X-Real-IP  $remote_addr;
        proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header  X-Forwarded-Proto  $scheme;
        proxy_set_header  Host $host:$server_port;

        include /etc/nginx/default.d/*.conf;
        location /status {
            stub_status on;
            access_log off;
        }
    [#list webapps?sort_by('contextPath') as webapp]
    [#if webapp.contextPath?length>1]
        location ${webapp.contextPath} {
            proxy_pass http://${entryPoints.get(webapp).name};
        }
    [/#if]
    [/#list]
    }
}

[#function addMargin(text)]
  [#assign lines=text?split('\n')]
  [#assign res =""/]
  [#list lines as line]
  [#assign res = res + ("        " + line?trim)]
  [#if line_has_next]
  [#assign res = res + "\n"]
  [/#if]
  [/#list]
  [#return res/]
[/#function]
