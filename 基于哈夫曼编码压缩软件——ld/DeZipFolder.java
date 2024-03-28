import java.io.*;
import java.util.*;

public class DeZipFolder {
    static FolderSearcher myfoldersearcher;
    static FolderInformationRelate myFolderInformationRelate;
    static ArrayList<String> splitterfilepath;
    static String mysrcDirectory;
    static ArrayList<String> mydirectoryStructure;
    static ArrayList<String> mysolozipsrcpath;

    public static void todezipFolder(String zipFolder,String dst) {//dst是目标父路径
        FileSplitter fileSplitter=new FileSplitter();
        splitterfilepath=new ArrayList<>();
        File file = new File(zipFolder);
       // String dezipparentpath=file.getParent();//解压出来的文件和压缩文件同一个父路径
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileType.equals("clzip")){
            System.out.println("这不是我创建的文件,无法解压,error1"); //检验压缩包来源是否是自己的压缩工具(第一重防护)
            return;
        }
        FileInputStream is = null;
        ObjectInputStream ois = null;
        try {
            is = new FileInputStream(zipFolder);
            ois = new ObjectInputStream(is);
            myfoldersearcher=(FolderSearcher)ois.readObject();
            long serializedSize=getserializedSize();
            myFolderInformationRelate=myfoldersearcher.folderInformationRelate;
            mysolozipsrcpath=myFolderInformationRelate.solozipsrcpath;//提取每个压缩文件的源文件绝对路径
            mysrcDirectory=myFolderInformationRelate.srcDirectory;//提取源文件夹绝对路径   //D:\1
            File f1=new File(mysrcDirectory);
            String fatherDirectoryName=f1.getName();
            String fatherDirectoryparentpath=f1.getParent();  //D:
            mydirectoryStructure=myFolderInformationRelate.directoryStructure;//提取文件夹的结构，各子绝对路径
            ArrayList<String> relatePath=getRelatePath();//各个子文件夹的与源文件夹的相对路径

            File f0= new File(dst+"\\"+fatherDirectoryName);//检查覆盖
            if(f0.exists()){
                Scanner sc=new Scanner(System.in);
                System.out.println("文件已存在，无需创建。请选择覆盖还是停止：1、停止。2或其他、覆盖");
                String choice=sc.next();
                if (choice.equals("1")){
                    ois.close();
                    is.close();
                    return;
                }
               deleteFolder(f0);
            }
            ArrayList<String> newDirectory=CreateNewDirectory.createNewDirectory(dst,relatePath,fatherDirectoryName);
            //新的文件夹的绝对路径（集）

            LinkedList<Long> mysoloziplenth=myFolderInformationRelate.soloziplenth;//提取每个压缩文件字节长度
            if(mysoloziplenth.isEmpty()){
                File file2=new File(zipFolder);
                file2.delete();
                return;
            }
            mysoloziplenth.addFirst(serializedSize);//得到每段文件的大小

            fileSplitter.splitFile(zipFolder,fatherDirectoryparentpath,mysoloziplenth);


            for (int i = 1; i <splitterfilepath.size() ; i++) {
                String newfile= splitterfilepath.get(i);//出来的是散列压缩包的绝对路径
                String solodst=mysolozipsrcpath.get(i-1).replace(fatherDirectoryparentpath,dst);
                File f=new File(solodst);
                solodst=f.getParent();
                DeZipFile.todezipFile(newfile,solodst);
               File file1=new File(newfile);
               file1.delete();
            }
            File file2=new File(splitterfilepath.get(0));
            file2.delete();





        } catch (Exception e) {
            System.out.println("这不是我创建的文件，无法解压,error2");//检验压缩包来源是否是自己的压缩工具(第二重防护)
        } finally {
            try {
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.println("你干嘛，哎哟2");
            }
        }
    }

    private static int getserializedSize() throws IOException {
        // 将反序列化的内容存储到 ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ooos = new ObjectOutputStream(baos);
        ooos.writeObject(myfoldersearcher);
        ooos.close();
        // 获取序列化内容在文件中所占的字节数
        byte[] serializedData = baos.toByteArray();
        return serializedData.length;
    }
    private static  ArrayList<String> getRelatePath(){
        ArrayList<String> relatepathofall=new ArrayList<>();
        for (String s : mydirectoryStructure) {
            relatepathofall.add(getsoloRelatePath(mysrcDirectory, s));//D:\1
        }
        return relatepathofall;
    }
    private static String getsoloRelatePath(String sourceFolderPath,String destinationFolderPath){//后面的参量更长
        String newStr = destinationFolderPath.replace(sourceFolderPath, "");
        newStr=newStr.substring(1);
        return newStr;
    }
    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            // 获取文件夹下的所有文件和子文件夹
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 递归删除子文件和子文件夹
                    deleteFolder(file);
                }
            }
        }

        // 删除空文件夹或文件
        folder.delete();
        System.out.println("文件夹删除成功：" + folder.getAbsolutePath());
    }


}
