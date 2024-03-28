import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean choosestop;
    public static void main(String[] args){

        while (true) {
            System.out.println("请确定你的操作：1.压缩文件，2.解压文件，3.压缩文件夹，4.解压文件夹，5.压缩包预览");
            String operatechoice = sc.next();
            switch (operatechoice) {
                case "1" -> operate1();
                case "2" -> operate2();
                case "3" -> operate3();
                case "4" -> operate4();
                case "5" -> operate5();
                default -> {
                    System.out.println("傻逼，叫你输入1,2");
                    continue;
                }
            }
            System.out.println("还继续吗：1.继续，2.退出");
            String operatechoice2 = sc.next();
            if (Objects.equals(operatechoice2, "1")) {
                System.out.println("回退到主页面");
            }else if (Objects.equals(operatechoice2, "2")) {
                System.out.println("成功退出");
                break;
            }else{
                System.out.println("傻逼，叫你输入1,2.直接退出得了");
                break;
            }
        }
    }
    public static void operate1(){
        System.out.println("你选择的操作是压缩文件，请输入待压缩文件完整路径(包括名字)");
        String tozip_srcFile = sc.next();
        ZipFile newzipFile = new ZipFile();
        System.out.println("请你指定压缩包的完整路径(包括压缩包名字)");
        String zipname=sc.next();
        long isstop=newzipFile.toZipFile(tozip_srcFile,zipname+".lzip","1");
        if(isstop==-1) {
            System.out.println("没压成，你选择了不覆盖");
        }
    }
    public static void operate2(){
        System.out.println("你选择的操作是解压文件，请输入待解压文件完整路径(包括压缩包名字)");
        String todezip_srcFile = sc.next();
        System.out.println("请你指定解压缩后的文件的父路径(不包括名字，名字自动还原)");
        String dst=sc.next();
        long startTime = System.currentTimeMillis();
        DeZipFile.todezipFile(todezip_srcFile,dst);
        long endTime = System.currentTimeMillis();
        long executionTime = (endTime - startTime);
        System.out.println("解压时间是" + executionTime + "ms");
    }
    public static void operate3(){
        System.out.println("你选择的操作是压缩文件夹，请输入待压缩文件夹完整路径(包括名字)");
        String tozip_srcFile = sc.next();
        FolderSearcher folderSearcher=new FolderSearcher(tozip_srcFile);
        System.out.println("请你指定压缩包的完整路径(包括压缩包名字)");
        String zipname=sc.next();//合并文件的路径
        long startTime = System.currentTimeMillis();
        folderSearcher.searchFile(folderSearcher.newFile);
        InformationTowrite.informationFilecreate(folderSearcher,tozip_srcFile+".myStructure");//创造一个文件存储文件夹的信息
        String concenFile=CreateFile.createMyFile(zipname+".clzip");//我的文件夹压缩包类型为clzip
        if(Main.choosestop){
            DeleteAllSoloZip.deleteAllsolozip();
            System.out.println("没压成，你选择了不覆盖");
            return;
        }
        FileMerger.mergeFiles(FolderSearcher.filespatharray,concenFile);
        DeleteAllSoloZip.deleteAllsolozip();
        long endTime = System.currentTimeMillis();
        long executionTime = (endTime - startTime);

        System.out.println("压缩文件夹压缩时间是" + executionTime + "ms");
        File sf=new File(tozip_srcFile);
        File rf=new File(concenFile);
        double ratioOfZip = (double) rf.length()/getFolderSize(sf);
        System.out.println("文件夹压缩率是" + (int) (ratioOfZip * 1000) / 10.0 + "%");

    }
    public static void operate4(){
        System.out.println("你选择的操作是解压文件夹，请输入待解压文件夹完整路径(包括名字)");
        String todezip_srcFile = sc.next();
        System.out.println("请你指定解压缩后的文件夹的父路径(不包括名字，名字自动还原)");
        String dst=sc.next();
        long startTime = System.currentTimeMillis();
        DeZipFolder.todezipFolder(todezip_srcFile,dst);
        long endTime = System.currentTimeMillis();
        long executionTime = (endTime - startTime);
        System.out.println("解压时间是" + executionTime + "ms");
    }
    public static void operate5(){
        System.out.println("你选择的操作是压缩包预览，请输入待待预览压缩包路径");
        String srcFile = sc.next();
        new ShowStructure().showStructure(srcFile);
    }
    public static long getFolderSize(File folder) {
        long size = 0;

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else {
                        size += getFolderSize(file);
                    }
                }
            }
        } else if (folder.isFile()) {
            size = folder.length();
        }
        return size;
    }

}

