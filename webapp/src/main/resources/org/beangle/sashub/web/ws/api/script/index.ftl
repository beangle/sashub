[#ftl attributes={'content_type':'text/x-shellscript'}]
#!/bin/sh

if [ "$(id -u)" == "0" ]; then
  echo "检查脚本只能运行在拥有普通权限的sudoer用户下。"
  exit 1
fi

if [[ ! "$(id)" == *"(wheel)"*  ]]; then
  echo "当前用户不是sudoer,请使用root进行添加."
  [#assign platform_name=platform.name?lower_case/]
  [#if platform_name?contains("fedora") || platform_name?contains("centos")]
  me="$(whoami)"
  echo "Using:usermod -aG wheel $me"
  exit 1
  [/#if]
fi

[#list scripts as script]
${script.scripts?replace('#!/bin/sh',"echo '正在检查${script.feature.name}....'")}
[/#list]
