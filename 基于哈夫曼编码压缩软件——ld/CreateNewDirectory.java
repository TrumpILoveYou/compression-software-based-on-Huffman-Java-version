import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateNewDirectory {
    public static ArrayList<String> createNewDirectory(String dstparentpath,ArrayList<String> relate_Path, String fatherDirectoryName) throws IOException {
        //创造文件夹到目标位置，此文件夹具有源文件夹的结构
        ArrayList<String> newDirectory=new ArrayList<>();
        String fatherabsolutepath=dstparentpath+"\\"+fatherDirectoryName;
        newDirectory.add(fatherabsolutepath);
        File f1 = new File(fatherabsolutepath);
        System.out.println(f1.mkdirs());
        for (String subfolderPath : relate_Path) {
            File subfolder = new File(fatherabsolutepath + "\\"+ subfolderPath);
            subfolder.mkdirs();
        }
        return newDirectory;

    }
}
