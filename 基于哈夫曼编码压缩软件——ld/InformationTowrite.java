import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class InformationTowrite {
    //创造文件，将“文件信息”类序列化输入
    public static void informationFilecreate(FolderSearcher folderSearcher,String path){
       // FolderInformationRelate newFolderInformationRelate=new FolderInformationRelate();
        CreateFile.createMyFile(path);//  C:\Users\19719\Desktop\FolderInformation
        File file=new File(path);
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new FileOutputStream(file);
            oos = new ObjectOutputStream(os);
            oos.writeObject(folderSearcher);
            FolderSearcher.filespatharray.addFirst(path);//在最前面插入文件夹结构文件路径
        }catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                oos.close();
                os.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
