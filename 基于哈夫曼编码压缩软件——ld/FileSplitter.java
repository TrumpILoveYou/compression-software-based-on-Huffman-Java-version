//import kotlinx.coroutines.repackaged.net.bytebuddy.dynamic.scaffold.MethodGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileSplitter {


    public void splitFile(String filePath, String Outpir,LinkedList<Long> blockSizes) {
        System.out.println("ngm");
        String dstpath;
        int numberofblocks=blockSizes.size();
        File f=new File(filePath);
        int buffersize=(int)f.length();

        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int blockSizeIndex = 0;
            long totalBytesRead = 0;

            byte[] buffer = new byte[buffersize];
            int dex=0;
            int bytesRead=0;

            while (true) {
                if(bytesRead != -1) bytesRead = bis.read(buffer,(int)totalBytesRead, buffersize-(int)totalBytesRead);
                if (bytesRead == -1&&dex==numberofblocks-1) {
                    break;
                }

                if(bytesRead!=-1)totalBytesRead += bytesRead;

                if (dex<numberofblocks-1) {
                    long remainingBytes = totalBytesRead - blockSizes.get(blockSizeIndex);
                    long tempt=blockSizes.get(blockSizeIndex);
                    dstpath=Outpir +"\\"+"block" + blockSizeIndex +  ".lzip";
                    FileOutputStream fos = new FileOutputStream(dstpath);
                    DeZipFolder.splitterfilepath.add(dstpath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(buffer, 0, (int)totalBytesRead - (int) remainingBytes);
                    bos.close();

                    blockSizeIndex++;
                    dex++;
                    totalBytesRead = remainingBytes;
                    System.arraycopy(buffer, (int)tempt, buffer, 0, (int) remainingBytes);
                }
            }

            // 处理最后一个块
            dstpath=Outpir +"\\"+"block" + blockSizeIndex +  ".lzip";
            FileOutputStream fos = new FileOutputStream(dstpath);
            DeZipFolder.splitterfilepath.add(dstpath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(buffer, 0, (int) totalBytesRead);
            bos.close();
            bis.close();
            fis.close();

            System.out.println("文档拆分完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
