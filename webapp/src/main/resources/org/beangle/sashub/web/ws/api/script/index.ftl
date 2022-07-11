[#ftl attributes={'content_type':'text/x-shellscript'}]
#!/bin/sh

if [ "$(id -u)" == "0" ]; then
  echo "检查脚本只能运行在拥有普通权限的sudoer用户下。"
  exit 1
fi

[#list scripts as script]
${script.scripts?replace('#!/bin/sh',"echo '正在检查${script.feature.name}....'")}
[/#list]
