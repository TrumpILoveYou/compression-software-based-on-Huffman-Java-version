import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class DeleteAllSoloZip {
    public static void deleteAllsolozip(){
        File f;
        LinkedList<String> myarray=FolderSearcher.filespatharray;
        for (String s : myarray) {
            f = new File(s);
            f.delete();
        }
    }
}
