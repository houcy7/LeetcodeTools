package com.houcy7.common;

/**
 * @ClassName Template
 * @Description TODO
 * @Author hou
 * @Date 2022/3/6 10:47 下午
 * @Version 1.0
 **/
public class Template {
    public static final String MAIN_JAVA =
            "\n" +
                    "public class Main {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        Solution solution = new Solution();\n" +
                    "\n" +
                    "    }\n" +
                    "}\n";

    public static final String SOLUTION_JAVA =
            "\n" +
                    "public class Solution {\n" +
                    "\n" +
                    "}";


    public static final String SOLUTION_MD =
            "### 方法\n" +
                    "\n" +
                    "#### 说明\n" +
                    "\n" +
                    "\n" +
                    "#### 思路与算法\n" +
                    "\n" +
                    "### 复杂度分析\n" +
                    "\n" +
                    "#### 时间复杂度\n" +
                    "\n" +
                    "#### 空间复杂度";

    public static final String TEMPLATE_MD =
            "---\n" +
                    "title: ${title}\n" +
                    "date: ${date}\n" +
                    "tags:\n" +
                    "- LeetCode\n" +
                    "categories:\n" +
                    "-  LeetCode\n" +
                    "---\n" +
                    "<!-- more -->\n" +
                    "\n" +
                    "${desc}\n" +
                    "\n" +
                    "------\n" +
                    "## 解题思路\n" +
                    "${solution}\n" +
                    "\n" +
                    "----\n" +
                    "## 代码\n" +
                    "\n" +
                    "```java\n" +
                    "${code}\n" +
                    "```\n" +
                    "\n" +
                    "----\n" +
                    "\n" +
                    "${link}\n";
}