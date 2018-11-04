package com.lxh.library.uitils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class DimenTool {
    public static void gen() {
        File file = new File("./library/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw240 = new StringBuilder();
        StringBuilder sw320 = new StringBuilder();
        StringBuilder sw384 = new StringBuilder();
        StringBuilder sw392 = new StringBuilder();
        StringBuilder sw400 = new StringBuilder();
        StringBuilder sw410 = new StringBuilder();
        StringBuilder sw432 = new StringBuilder();
        StringBuilder sw533 = new StringBuilder();
        StringBuilder sw592 = new StringBuilder();
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw640 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw820 = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));

            String tempString;
            int line = 1;

            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw240.append(start).append(formatDouble2( num * 0.67)).append(end).append("\r\n");
                    sw360.append(start).append(num * 1).append(end).append("\r\n");
                    sw480.append(start).append(formatDouble2(num * 1.34)).append(end).append("\r\n");
                    sw640.append(start).append(formatDouble2(num * 1.78)).append(end).append("\r\n");
                    sw720.append(start).append(formatDouble2(num * 2)).append(end).append("\r\n");
                    sw820.append(start).append(formatDouble2(num * 2.27)).append(end).append("\r\n");


                    sw320.append(start).append(formatDouble2(num * 0.89)).append(end).append("\r\n");
                    sw384.append(start).append(formatDouble2(num * 1.067)).append(end).append("\r\n");
                    sw392.append(start).append(formatDouble2(num * 1.088)).append(end).append("\r\n");
                    sw400.append(start).append(formatDouble2(num * 1.11)).append(end).append("\r\n");
                    sw410.append(start).append(formatDouble2(num * 1.14)).append(end).append("\r\n");
                    sw432.append(start).append(formatDouble2(num * 1.2)).append(end).append("\r\n");


                    sw533.append(start).append(formatDouble2(num * 1.48)).append(end).append("\r\n");
                    sw592.append(start).append(formatDouble2(num * 1.644)).append(end).append("\r\n");
                } else {
                    sw240.append(tempString).append("");
                    sw480.append(tempString).append("");
                    sw640.append(tempString).append("");
                    sw720.append(tempString).append("");
                    sw360.append(tempString).append("");
                    sw820.append(tempString).append("");

                    sw320.append(tempString).append("");
                    sw384.append(tempString).append("");
                    sw392.append(tempString).append("");
                    sw400.append(tempString).append("");
                    sw410.append(tempString).append("");
                    sw432.append(tempString).append("");
                    sw533.append(tempString).append("");
                    sw592.append(tempString).append("");
                }
                line++;

            }
            reader.close();
            String sw240file = "./library/src/main/res/values-sw240dp";
            String sw480file = "./library/src/main/res/values-sw480dp";
            String sw640file = "./library/src/main/res/values-sw640dp";
            String sw720file = "./library/src/main/res/values-sw720dp";
            String sw360file = "./library/src/main/res/values-sw360dp";
            String sw820file = "./library/src/main/res/values-sw820dp";

            String sw320file = "./library/src/main/res/values-sw320dp";
            String sw384file = "./library/src/main/res/values-sw384dp";
            String sw392file = "./library/src/main/res/values-sw392dp";
            String sw400file = "./library/src/main/res/values-sw400dp";
            String sw410file = "./library/src/main/res/values-sw410dp";
            String sw432file = "./library/src/main/res/values-sw432dp";
            String sw533file = "./library/src/main/res/values-sw533dp";
            String sw592file = "./library/src/main/res/values-sw592dp";

            //将新的内容，写入到指定的文件中去
            writeFile(sw240file, sw240.toString());
            writeFile(sw480file, sw480.toString());
            writeFile(sw640file, sw640.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw360file, sw360.toString());
            writeFile(sw820file, sw820.toString());

            writeFile(sw320file, sw320.toString());
            writeFile(sw384file, sw384.toString());
            writeFile(sw392file, sw392.toString());
            writeFile(sw400file, sw400.toString());
            writeFile(sw410file, sw410.toString());
            writeFile(sw432file, sw432.toString());

            writeFile(sw533file, sw533.toString());
            writeFile(sw592file, sw592.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static double formatDouble2(double d) {
        // 旧方法，已经不再推荐使用
//        BigDecimal bg = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);


        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);


        return bg.doubleValue();
    }

    /**
     * 写入方法
     */
    private static void writeFile(String file, String text) {
        try {
            File file1 = new File(file);
            if (!file1.exists()){
                file1.mkdirs();
            }
            File file2 = new File(file1,"dimens.xml");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file2)));
            out.println(text);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        gen();
    }
}
