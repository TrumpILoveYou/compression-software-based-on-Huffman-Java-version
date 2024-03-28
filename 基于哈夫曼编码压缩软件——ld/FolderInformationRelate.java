import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

//先不压缩试一试
public class FolderInformationRelate implements Serializable {
    //记录每个文件的压缩包长度，以及初始文件夹的结构
    public String srcDirectory;//源文件夹路径
    public  ArrayList<String> directoryStructure;//文件夹的结构
    public LinkedList<Long> soloziplenth;//每个压缩文件字节长度
    public ArrayList<String> solozipsrcpath;//每个压缩文件的源文件绝对路径
    public ArrayList<String> treeshow;//方便树形结构展示
    public FolderInformationRelate(String srcDirectory) {
        this.srcDirectory = srcDirectory;
    }
}
