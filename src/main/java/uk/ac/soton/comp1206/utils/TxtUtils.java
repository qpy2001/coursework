package uk.ac.soton.comp1206.utils;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
                List<Pair<String,Integer>> gradesRecord = new ArrayList<>();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    String[] arr = text.split(" ");
                    Pair<String,Integer> pair = new Pair<>(arr[0], Integer.valueOf(arr[1]));
                    gradesRecord.add(pair);
                }
                return gradesRecord;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }




    /**使用FileOutputStream来写入txt文件
     * @param txtPath txt文件路径
     * @param content 需要写入的文本
     */
    public static void writeTxt(String txtPath,String content){
        FileOutputStream fileOutputStream = null;
        File file = new File(txtPath);
        try {
            if(file.exists()){
                //判断文件是否存在，如果不存在就新建一个txt
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
