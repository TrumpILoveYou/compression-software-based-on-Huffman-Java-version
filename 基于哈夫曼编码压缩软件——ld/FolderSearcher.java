

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class FolderSearcher implements Serializable {
    public static LinkedList<String> filespatharray;//所有压缩文件路径的集合
    FolderInformationRelate folderInformationRelate;
    File newFile;
    public FolderSearcher(String srcFile) {
        this.newFile=new File(srcFile);
        filespatharray=new LinkedList<>();
        folderInformationRelate=new FolderInformationRelate(srcFile);
        folderInformationRelate.directoryStructure=new ArrayList<>();
        folderInformationRelate.soloziplenth=new LinkedList<>();
        folderInformationRelate.solozipsrcpath=new ArrayList<>();
        folderInformationRelate.treeshow=new ArrayList<>();
    }
    public void searchFile(File dir){
        // 判断dir是否为文件夹
        if(dir!=null && dir.isDirectory()){
            // 提取当前目录下的一级文件对象
            File[] files = dir.listFiles();
            //判断是否存在一级文件对象
            if (files != null){
                for (File file : files) {
                    // 判断当前的一级对象是文件还是文件夹
                    if (file.isFile()){
                        //如果是文件，相关操作
                        folderInformationRelate.solozipsrcpath.add(file.getAbsolutePath());
                        folderInformationRelate.treeshow.add(file.getAbsolutePath());
                        ZipFile newzipFile = new ZipFile();
                        long len=newzipFile.toZipFile(file.getAbsolutePath(),file.getAbsolutePath()+".lzip","3");
                        folderInformationRelate.soloziplenth.add(len);
                    }else if(file.isDirectory()){
                        folderInformationRelate.treeshow.add(file.getAbsolutePath());
                        folderInformationRelate.directoryStructure.add(file.getAbsolutePath());
                        // 是文件夹，递归查找
                        searchFile(file);
                    }
                }
            }
        }else{
            System.out.println(dir.getAbsolutePath()+ "---------搜索的不是文件夹，不支持");
        }
    }
}

