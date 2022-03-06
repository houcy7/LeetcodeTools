#!/bin/bash
# author:houcy7

if [[ "-d" = $1  || "-download" = $1 ]]; then
	java -jar /Users/hou/env/leetcode/tools.jar download
elif [[ "-g" = $1  || "-generate" = $1 ]]; then
	java -jar /Users/hou/env/leetcode/tools.jar generate
elif [[ "-p" = $1  || "-publish" = $1 ]]; then
	cd /Users/hou/houcy7/blog
	git add .
	git commit -m 'submit leetcode note'
	sleep 3s
	git push
elif [[ "-pc" = $1  || "-publishcode" = $1 ]]; then
	cd /Users/hou/houcy7/leetcode
	git add .
	git commit -m 'submit code'
	sleep 3s
	git push
elif [[ "-gp" = $1 ]]; then
	java -jar /Users/hou/env/leetcode/tools.jar generate
	cd /Users/hou/houcy7/blog
	git add .
    git commit -m 'submit leetcode note'
    sleep 1s
    git push
    sleep 2s
	cd /Users/hou/houcy7/leetcode
	git add .
	git commit -m 'submit code'
	sleep 1s
	git push
elif [[ "-h" = $1  || "-help" = $1 ]]; then
echo "
用法：leetcode [-options]
其中选项包括：
	-d	下载力扣题目详情并生成解题代码
	-download	下载力扣题目详情并生成解题代码
	-g	生成解题笔记
	-generate	生成解题笔记
	-p	发布笔记
	-publish 发布笔记
	-pc	发布代码
	-publishcode 发布代码
	-gp	生成解题笔记并发布
"
else
	echo "Unsupported command"
fi
