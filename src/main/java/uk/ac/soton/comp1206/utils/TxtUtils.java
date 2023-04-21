package uk.ac.soton.comp1206.utils;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TxtUtils {
    /**传入txt路径读取txt文件
     * @param txtPath
     * @return 返回读取到的内容
     */
    public static List readTxt(String txtPath) {
        File file = new File(txtPath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                List<String> gradesRecord = new ArrayList<>();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    gradesRecord.add(text);
                }
                gradesRecord.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });

                return gradesRecord;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getMaxScores(String txtPath){
        List<String> temp = readTxt(txtPath);
        try {
            return temp.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    /**使用FileOutputStream来写入txt文件
     * @param txtPath txt文件路径
     * @param content 需要写入的文本
     */
    public static void writeTxt(String txtPath,String content){
        File file = new File(txtPath);
        try {
            if(file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(txtPath, true);
            writer.write(content+System.getProperty("line.separator"));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
