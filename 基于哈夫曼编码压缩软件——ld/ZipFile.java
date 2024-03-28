import java.io.*;


public class ZipFile {
    FileInformationRelate newfileinformation;
    static int[] addzero;
    static boolean choosestop;
    HuffmanCode newCode;
    byte[] contentin;
    public long toZipFile(String srcFile,String zipname,String operatechoice) {
        choosestop=false;
        File file = new File(srcFile);
        long fileSize = file.length(); // 获取文件大小
        long startTime = System.currentTimeMillis();
        double ratioOfZip;
        toCreateFileForZip(srcFile);
        long codeLen=0;

        String dstFile = CreateFile.createMyFile(zipname);
        if (choosestop)return -1;
        if (operatechoice.equals("3")){
            FolderSearcher.filespatharray.add(dstFile);//把这个部分压缩文件的路径存进去
        }
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            os = new FileOutputStream(dstFile);
            oos = new ObjectOutputStream(os);
            //读取初始文档
            contentin=new byte[(int)fileSize];
            int dex=0;
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(srcFile))) {
                int byteRead;
                while ((byteRead = inputStream.read()) != -1) {
                    contentin[dex]=((byte) byteRead);
                    dex++;
                }
            }
            //newfileinformation.path=srcFile;
            if (contentin.length == 0) {
                newfileinformation.isempty = true;
                oos.writeObject(newfileinformation);
                System.out.println("成功压缩");
                File f=new File(dstFile);
                return f.length();
            }
            newCode = new HuffmanCode(contentin);
            newCode.Huffmandisplay();
            oos.writeObject(newfileinformation);
            oos.writeObject(newCode.mymap);

            //写入压缩文档

            int partsize=1000;
            byte[][] splitcontein=splitArray(contentin,partsize);
            ReadTimes newTimes=new ReadTimes();
            newTimes.readtimes=splitcontein.length;
            addzero=new int[splitcontein.length];
            oos.writeObject(newTimes);
            for (int i = 0; i < splitcontein.length; i++) {
                byte []result=zip(splitcontein[i],i);
                oos.writeObject(result);
            }
            ZeroAdd myzeroadd=new ZeroAdd();
            myzeroadd.zeroadd=addzero;
            oos.writeObject(myzeroadd);
            File newfile=new File(dstFile);
            codeLen=newfile.length();
             System.out.println("编码写入文件成功！");
            ratioOfZip = (double) codeLen/contentin.length;
            System.out.println("压缩率是" + (int) (ratioOfZip * 1000) / 10.0 + "%"); //保留一位小数
            long endTime = System.currentTimeMillis();
            long executionTime = (endTime - startTime);
            System.out.println("压缩时间是" + executionTime + "ms");
            System.out.println("成功压缩");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                oos.close();
                os.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return codeLen;
    }

    public void toCreateFileForZip(String srcFile){
        File file = new File(srcFile);
        String parentpath = file.getParent();
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        newfileinformation = new FileInformationRelate(parentpath, fileName, fileType);
        newfileinformation.isempty = false;
    }
    private  byte[] zip(byte[] bytes,int dex) {
        //1.利用 huffmanCodes 将 bytes 转成赫夫曼编码对应的字符串
        StringBuilder mystring= new StringBuilder();
        //遍历 bytes 数组
        for (byte b : bytes) {
            mystring.append(newCode.mymap.get(b));
        }
        while (mystring.length()%8!=0){
            mystring.append("0");
            addzero[dex]++;
        }
        String str=mystring.toString();
        String[] str8=new String[str.length()/8];
        for (int i = 0; i < str.length()/8; i++) {
            str8[i]=str.substring(i*8,i*8+8);
        }
        byte[] by=new byte[str8.length];
        for (int i = 0; i < str8.length; i++) {
            String exp=str8[i];
            byte b=0;
            for (int j = 0; j < 8; j++) {
                if (exp.charAt(j) == '1') {
                    b |= (1 << (7 - j));
                }
            }
            by[i]=b;
        }
        return by;


    }
    public static byte[][] splitArray(byte[] array, int chunkSize) {
        int numOfChunks = (int) Math.ceil((double) array.length / chunkSize);
        byte[][] chunks = new byte[numOfChunks][];
        for (int i = 0; i < numOfChunks; i++) {
            int start = i * chunkSize;
            int length = Math.min(array.length - start, chunkSize);
            byte[] chunk = new byte[length];
            System.arraycopy(array, start, chunk, 0, length);
            chunks[i] = chunk;
        }
        return chunks;
    }
}


