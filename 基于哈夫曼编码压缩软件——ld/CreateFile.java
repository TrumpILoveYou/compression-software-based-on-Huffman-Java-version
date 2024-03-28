import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateFile {
    //根据提供的文件绝对路径创造文件，返回所创造文件的路径
    public static String createMyFile(String fileName){
        File file = new File(fileName);
        Scanner sc=new Scanner(System.in);
        try {
            if (file.createNewFile()) {
                System.out.println("文件已成功创建！");
            } else {
                System.out.println("文件已存在，无需创建。请选择覆盖还是停止：1、停止。2或其他、覆盖");
                String choice=sc.next();
                if (choice.equals("1")){
                    Main.choosestop=true;
                    ZipFile.choosestop=true;
                    DeZipFile.choosestop=true;
                }
            }
        } catch (IOException e) {
            System.out.println("创建文件时出现错误：" + e.getMessage());
        }
        return fileName;
    }
}
