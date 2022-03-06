package com.houcy7.action;

import com.google.common.io.Files;
import com.houcy7.common.Constant;
import com.houcy7.common.Template;
import okhttp3.*;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * @ClassName Download
 * @Description TODO
 * @Author hou
 * @Date 2022/3/6 6:09 下午
 * @Version 1.0
 **/
public class Download {

    static Scanner scanner = new Scanner(System.in);

    public static void download() throws Exception {
        JSONObject jsonObj;
        JSONObject dataObj;

        while (true) {
            System.out.println("请输入LeetCode中题目的英文名或路径：");
            Scanner scanner = new Scanner(System.in);
            String questionName = scanner.next();
            if (questionName.contains("//")) {
                String[] split = questionName.split("/");
                questionName = split[split.length - 1];
            }
            String questionStr = getLeetCodeQuestionStr(questionName);

            jsonObj = new JSONObject(questionStr);
            dataObj = (JSONObject) jsonObj.get("data");

            if ("null".equals(dataObj.getString("question"))) {
                System.out.println("题目不存在！(0.退出 1：输入新题目)");
                String choose;
                while (true) {
                    choose = scanner.next();
                    if ("0".equals(choose)) {
                        System.exit(0);
                    } else if ("1".equals(choose)) {
                        break;
                    } else {
                        System.out.println("无效输入，请重新选择(0.退出 1：输入新题目)");
                    }
                }
            } else {
                break;
            }
        }


        // 提取题目基本信息
        JSONObject questionObj = (JSONObject) dataObj.get("question");
        String questionFrontendId = questionObj.getString("questionFrontendId");
        String translatedTitle = questionObj.getString("translatedTitle");
        String titleSlug = questionObj.getString("titleSlug");
        String difficulty = questionObj.getString("difficulty");
        String questionContent = questionObj.getString("translatedContent");

        String questionContentHead = "## " + questionFrontendId + "." + translatedTitle + "\n\n";
        questionContentHead = questionContentHead + "**难度：** " + difficulty + "\n\n" + "---" + "\n\n";
        questionContent = questionContentHead + questionContent;

        System.out.println("************已获取到题目数据************");
        System.out.println("-------------------------------------");
        System.out.println("题号：" + questionFrontendId);
        System.out.println("题目：" + translatedTitle);
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("************开始生成本地项目************");
        System.out.println("-------------------------------------");


        // 创建解题文件夹
        String folderPath = autoCreateFolder(questionFrontendId, titleSlug);

        // 创建题目markdown文件
        String markdownQuestionName = String.format("no%s_%s", questionFrontendId, translatedTitle);
        String questionPath = folderPath + "/" + markdownQuestionName;

        File mdFile = new File(questionPath + ".md");
        File parent = mdFile.getParentFile();
        if (!parent.exists()) {
            Files.createParentDirs(mdFile);
        }
        Files.write(questionContent, mdFile, StandardCharsets.UTF_8);

        System.out.println("题目描述md输出完成");
        System.out.println("题目已克隆到本地");

        // 创建Solution.java文件
        copyFile("Solution.java", Template.SOLUTION_JAVA, folderPath);
        copyFile("Main.java", Template.MAIN_JAVA, folderPath);
        copyFile("Solution.md", Template.SOLUTION_MD, folderPath);

        System.out.println("项目初始化完成，可开始解题！");
        System.out.println("-------------------------------------");
    }

    private static void copyFile(String name, String template, String folderPath) throws IOException {
        if (name.endsWith(".java")) {
            String packageName = getPackageName(folderPath);
            template = packageName + "\n" +  template;
        }
        Files.write(template, new File(folderPath + name), StandardCharsets.UTF_8);
        System.out.println("已生成" + name);

    }

    private static String getPackageName(String newPath) {
        String[] newPathSplit = newPath.split("/");
        String packageStr = "package ";

        packageStr += newPathSplit[newPathSplit.length - 4] + ".";
        packageStr += newPathSplit[newPathSplit.length - 3] + ".";
        packageStr += newPathSplit[newPathSplit.length - 2] + ".";
        packageStr += newPathSplit[newPathSplit.length - 1] + ";";
        return packageStr;
    }

    public static String getLeetCodeQuestionStr(String questionName) throws IOException {
        String questionUrl = "https://leetcode-cn.com/problems/" + questionName + "/description/";
        String graphqlUrl = "https://leetcode-cn.com/graphql/";
        Connection.Response response = Jsoup.connect(questionUrl)
                .method(Connection.Method.GET)
                .execute();

        String csrftoken = "g2B7f2I2PVERrR160QCgLvdTeX2CY2KWXoiXtsNlNMxrNzSsNA5esgVVrhvx4PZs";
        String __cfduid = response.cookie("__cfduid");

        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        String postBody = "query{\n" +
                "  question(titleSlug:\"" + questionName + "\") {\n" +
                "    questionFrontendId\n" +
                "    translatedTitle\n" +
                "    titleSlug\n" +
                "    difficulty\n" +
                "    translatedContent\n" +
                "  }\n" +
                "}\n";

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/graphql")
                .addHeader("Referer", questionUrl)
                .addHeader("Cookie", "__cfduid=" + __cfduid + ";" + "csrftoken=" + csrftoken)
                .addHeader("x-csrftoken", csrftoken)
                .url(graphqlUrl)
                .post(RequestBody.create(MediaType.parse("application/graphql; charset=utf-8"), postBody))
                .build();

        Response response1 = client.newCall(request).execute();
        return response1.body().string();
    }

    public static String autoCreateFolder(String questionFrontendId, String titleSlug) throws IOException {
        // 文件夹的名称
        String folderName = "no" + questionFrontendId;
        folderName = folderName + "_" + titleSlug.replace("-", "_");

        // 创建文件夹
        String folderPath = Constant.QUESTION_LOCATION;
        folderPath = getPath(folderPath) + folderName + "/";

        System.out.println("项目位置：" + folderPath);
        return folderPath;
    }

    private static String getPath(String folderName) {
        Calendar calendar = Calendar.getInstance();
        return String.format("%s/year%s/month%s/date%s/", folderName, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH) + 1);
    }

    public static void main(String[] args) throws Exception {
        Download.download();
    }
}