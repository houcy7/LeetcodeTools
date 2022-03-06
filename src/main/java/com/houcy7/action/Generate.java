package com.houcy7.action;

import com.google.common.io.Files;
import com.houcy7.common.Constant;
import com.houcy7.common.Template;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName Generate
 * @Description TODO
 * @Author hou
 * @Date 2022/3/6 6:11 下午
 * @Version 1.0
 **/
public class Generate {

    public static void generate() throws IOException {
        Set<String> existsNoteSet = getExistsNodeSet();
        String template = Template.TEMPLATE_MD;
        System.out.println("加载解题记录模板完成");

        File file = new File(Constant.QUESTION_LOCATION);
        File[] yearFiles = file.listFiles();
        for (File yearFile : yearFiles) {
            if (!yearFile.isDirectory()) {
                continue;
            }

            File[] monthFiles = yearFile.listFiles();
            for (File monthFile : monthFiles) {
                if (!monthFile.isDirectory()) {
                    continue;
                }

                File[] dayFiles = monthFile.listFiles();
                for (File dayFile : dayFiles) {
                    if (!dayFile.isDirectory()) {
                        continue;
                    }

                    File[] questionFiles = dayFile.listFiles();
                    for (File questionFile : questionFiles) {
                        if (!questionFile.isDirectory()) {
                            continue;
                        }

                        File[] files = questionFile.listFiles();
                        if (files.length != 4) {
                            continue;
                        }
                        String noteName = getNoteName(questionFile);
                        String content = template;
                        if(existsNoteSet.contains(noteName + ".md")){
                            continue;
                        }
                        for (File file1 : files) {
                            String name = file1.getName();
                            if (name.startsWith("no")) {
                                String file1Name = file1.getName().split("_")[1];
                                String title = file1Name.substring(0, file1Name.lastIndexOf("."));
                                // 标题
                                content = replace(content, "title", title);
                                // 日期
                                content = replace(content, "date", getNow());
                                // 描述
                                content = replace(content, "desc", file1);
                            } else if ("Solution.md".equals(name)) {
                                content = replace(content, "solution", file1);
                            } else if ("Solution.java".equals(name)) {
                                content = replace(content, "code", file1);
                            }
                        }
                        content = replace(content, "link", "[相关链接]("
                                + getLink(yearFile, monthFile, dayFile, questionFile) + ")");
                        Files.write(content, new File(Constant.NOTE_LOCATION + noteName + ".md"), StandardCharsets.UTF_8);
                        System.out.println(String.format("输出解题md 《%s》完成", noteName));
                    }
                }
            }
        }
    }

    private static String getLink(File yearFile, File monthFile, File dayFile, File questionFile) {
        return String.format("https://github.com/houcy7/leetcode/tree/master/src/main/%s/%s/%s/%s",
                yearFile.getName(), monthFile.getName(), dayFile.getName(), questionFile.getName());
    }

    private static Set<String> getExistsNodeSet() {
        File file = new File(Constant.NOTE_LOCATION);
        Set<String> set = new HashSet<>();
        File[] files = file.listFiles();
        for (File file1 : files) {
            set.add(file1.getName());
        }
        return set;
    }

    /**
     * 文件名称
     * @param questionFile
     * @return
     */
    private static String getNoteName(File questionFile) {
        String name = questionFile.getName();
        return name.substring(name.indexOf("_") + 1);
    }

    private static String replace(String content, String pattern, String to, boolean fromFile, File file) throws IOException {
        pattern = String.format("${%s}", pattern);
        if (fromFile) {
            List<String> list = Files.readLines(file, StandardCharsets.UTF_8);
            return content.replace(pattern, String.join("\n", list));
        } else {
            return content.replace(pattern, to);
        }
    }

    private static String replace(String content, String pattern, File file1) throws IOException {
        return replace(content, pattern, null, true, file1);
    }

    private static String replace(String content, String pattern, String to) throws IOException {
        return replace(content, pattern, to, false, null);
    }

    private static String getNow() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf2.format(time);
    }


    public static void main(String[] args) throws IOException {
        Generate.generate();
    }
}