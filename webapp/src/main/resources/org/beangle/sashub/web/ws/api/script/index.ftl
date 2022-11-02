[#ftl attributes={'content_type':'text/x-shellscript'}]
#!/bin/bash

if [ "$(id -u)" == "0" ]; then
  echo "检查脚本只能运行在拥有普通权限的sudoer用户下。"
  exit 1
fi

[#assign sudoer_group="sudo"/]
[#assign platform_name=platform.name?lower_case/]
[#if platform_name?contains("fedora") || platform_name?contains("centos")]
[#assign sudoer_group="wheel"/]
[/#if]

if [[ ! "$(id)" == *"(${sudoer_group})"*  ]]; then
  echo "当前用户不是sudoer,请使用root进行添加."
  me="$(whoami)"
  echo "Using:usermod -aG ${sudoer_group} $me"
  exit 1
fi

[#list scripts as script]

${script.scripts?replace('#!/bin/bash',"echo '正在检查${script.feature.name}....'")}
[/#list]
