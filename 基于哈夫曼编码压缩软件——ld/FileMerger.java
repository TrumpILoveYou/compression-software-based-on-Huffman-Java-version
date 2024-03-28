import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class FileMerger {
    public static void mergeFiles(LinkedList<String> fileNames, String mergedFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(mergedFileName);
            for (String fileName : fileNames) {
                FileInputStream fis = new FileInputStream(fileName);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fis.close();
            }
            fos.close();

            System.out.println("文件合并成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
