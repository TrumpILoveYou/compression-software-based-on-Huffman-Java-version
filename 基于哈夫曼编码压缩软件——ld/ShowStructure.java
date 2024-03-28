import java.io.*;
import java.util.*;

public class ShowStructure {
    static ArrayList<String> mydirectoryStructure;
    static String mysrcDirectory;
    static ArrayList<String> mytreeshow;
    static int totalnum;
    static boolean []isvisted;

    public void showStructure(String zip) {
        totalnum=0;
        File file = new File(zip);
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileType.equals("clzip") && !fileType.equals("lzip")) {
            System.out.println("这不是我创建的文件,无法预览,error1"); //检验压缩包来源是否是自己的压缩工具(第一重防护)
            return;
        }
        if (fileType.equals("lzip")) {
            showStructureOfFile(zip);
        }
        if (fileType.equals("clzip")) {
            showStructureOfFolder(zip);
        }
    }

    public static void showStructureOfFile(String src) {
        FileInputStream is = null;
        ObjectInputStream ois = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(src);
            ois = new ObjectInputStream(is);
            FileInformationRelate getinformation = (FileInformationRelate) ois.readObject();
            System.out.println("_____________________________________________");
            System.out.println(getinformation.parentpath + "\\" + getinformation.filename );
            System.out.println("_____________________________________________");
        } catch (Exception e) {
            System.out.println("这不是我创建的文件，无法解压,error2");//检验压缩包来源是否是自己的压缩工具(第二重防护)
        } finally {
            try {
                os.close();
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.print("");
            }
        }
    }

    public void showStructureOfFolder(String src) {
        FolderSearcher myfoldersearcher;
        FolderInformationRelate myFolderInformationRelate;

        ArrayList<String> mysolozipsrcpath;
        FileInputStream is = null;
        ObjectInputStream ois = null;
        try {
            is = new FileInputStream(src);
            ois = new ObjectInputStream(is);
            myfoldersearcher = (FolderSearcher) ois.readObject();
            myFolderInformationRelate = myfoldersearcher.folderInformationRelate;

            mysolozipsrcpath = myFolderInformationRelate.solozipsrcpath;//提取每个压缩文件的源文件绝对路径
            mysrcDirectory = myFolderInformationRelate.srcDirectory;//提取源文件夹绝对路径   //D:\1
            mydirectoryStructure = myFolderInformationRelate.directoryStructure;//提取文件夹的结构，各子绝对路径
            mytreeshow = myFolderInformationRelate.treeshow;

            LinkedList<String> relatePath = getRelatePath();//各个子类的与源文件夹的相对路径
            relatePath.addFirst("");
            isvisted=new boolean[relatePath.size()];
            //核心代码：
            System.out.println("_____________________________________________");
            System.out.println(mysrcDirectory);
            String prebranch = "|——";
            isvisted[0]=true;
            int t=1;
            while (t<relatePath.size()) {
                if(!relatePath.get(t).contains("\\"))subStructure(relatePath, t, prebranch);
                t++;
            }
            System.out.println("_____________________________________________");
        } catch (Exception e) {
            System.out.println("这不是我创建的文件，无法预览,error2");//检验压缩包来源是否是自己的压缩工具(第二重防护)
        } finally {
            try {
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.println("你干嘛，哎哟2");
            }
        }
    }

    private static LinkedList<String> getRelatePath() {
        LinkedList<String> relatepathofall = new LinkedList<>();
        for (String s : mytreeshow) {
            relatepathofall.add(getsoloRelatePath(mysrcDirectory, s));//D:\1
        }
        return relatepathofall;
    }

    private static String getsoloRelatePath(String sourceFolderPath, String destinationFolderPath) {//后面的参量更长
        String newStr = destinationFolderPath.replace(sourceFolderPath, "");
        newStr = newStr.substring(1);
        return newStr;
    }
    public void subStructure(LinkedList<String> treestr, int index, String prebranch)  {
        if (!isvisted[index])System.out.println(prebranch + getPureName(treestr.get(index)));
        isvisted[index]=true;
        if (treestr.size() == 1) return;
        index++;
        int t=index;
        while (t<treestr.size()) {
            if(isFatherPath(treestr.get(index - 1),treestr.get(t)))subStructure(treestr, t, "|   " + prebranch);
            t++;
        }
    }
    public boolean isFatherPath(String likeFather,String likeSon){
        File f=new File(mysrcDirectory+"\\"+likeSon);
        return (mysrcDirectory+"\\"+likeFather).equals(f.getParent());
    }
    public String getPureName(String rawString){
        if(!rawString.contains("\\"))return rawString;
        int lastIndex = rawString.lastIndexOf("\\");
        return rawString.substring(lastIndex);

    }

}

