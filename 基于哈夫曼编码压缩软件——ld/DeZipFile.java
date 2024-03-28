import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeZipFile {
    static Map<Byte, String> mymap;
    static Map<String, Byte> newmap;
    static ZeroAdd zerotodelete;
    static FileInformationRelate getinformation;
    static ReadTimes newReadTimes;
    static boolean choosestop;
    public static void todezipFile(String zipFile,String dstparent) {
        File file = new File(zipFile);

        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileType.equals("lzip")){
            System.out.println("这不是我创建的文件,无法解压,error1"); //检验压缩包来源是否是自己的压缩工具(第一重防护)
            return;
        }
        FileInputStream is = null;
        ObjectInputStream ois = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(zipFile);
            ois = new ObjectInputStream(is);
            getinformation=(FileInformationRelate) ois.readObject();

            String dstFile=CreateFile.createMyFile(dstparent+"\\"+getinformation.filename);
            if (choosestop){
                os.close();
                ois.close();
                is.close();
                return ;
            }
            os = new FileOutputStream(dstFile);
            if(getinformation.isempty){
                System.out.println("成功解压");
                return;
            }
            mymap = (Map<Byte, String>) ois.readObject();
            newmap = new HashMap<>();
            for (Map.Entry<Byte, String> entry : mymap.entrySet()) {
                newmap.put(entry.getValue(), entry.getKey());
            }
            newReadTimes=(ReadTimes)ois.readObject();
            int idex=0;
            byte[][] huffmanBytes=new byte[newReadTimes.readtimes][];
            while (idex<newReadTimes.readtimes) {
                   huffmanBytes[idex] = (byte[]) ois.readObject();
                    idex++;
            }
            zerotodelete=(ZeroAdd) ois.readObject();
            for (int i = 0; i < zerotodelete.zeroadd.length; i++) {
                byte[] towrite=decode(huffmanBytes[i],zerotodelete.zeroadd[i]);
                os.write(towrite);
            }
            System.out.println("成功解压");
        } catch (Exception e) {
            System.out.println("这不是我创建的文件，无法解压,error2");//检验压缩包来源是否是自己的压缩工具(第二重防护)
        } finally {
            try {
                assert os != null;
                os.close();
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.println("你干嘛，哎哟");
            }
        }
    }
    private static byte[] decode(byte[] huffmanBytes,int dele) {
        String myString;
        //2.将byte数组转成二进制的字符串
        StringBuilder binaryString = new StringBuilder();

        for (byte b : huffmanBytes) {
            String binary = Integer.toBinaryString(b & 0xFF); // 将字节转换为二进制字符串
            while (binary.length() < 8) {
                binary = "0" + binary; // 在前面补零，使其长度为8位
            }
            binaryString.append(binary); // 将二进制字符串连接起来
        }

        myString=  binaryString.substring(0,  binaryString.length()-dele);
        //创建要给的集合,存放byte
        List<Byte> list = new ArrayList<>();
        //i可以理解为,就是一个索引,不停的扫描stringBuilder=10101000010111...
        for (int i = 0; i < myString.length(); ) {
            int count = 1;//小的计数器
            boolean flag = true;
            Byte b = null;
            while (flag) {
                //递增地取出一个'1' '0' 101000010111...
                String key = myString.substring(i, i + count);//i 不动,让count移动,直到匹配到一个字符
                b = newmap.get(key);
                if (b == null) {//说明还没匹配到字符
                    count++;
                } else {//说明已经匹配到
                    flag = false;
                }
            }
            list.add(b);
            //i += count-1;//i直接移动到 count 的位置
            i += count;
        }
        //当for循环结束后,我们list中就存放了所有的字符 i like java
        //把list中的数据,放入到byte[] 并返回
        byte[] b = new byte[list.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = list.get(i);
        }
        return b;
    }
    private static String byteToBitString(boolean flag, byte b) {
        //使用遍历保存b
        int temp = b;//将b转成int
        //如果是正数,我们还存在补高位
        if (flag) {
            temp |= 256;//按为与 256 1 0000 0000  |   0000 00001 => 1 0000 0001
            // temp 1 => 000
        }


        String str = Integer.toBinaryString(temp);//注意!!此处返回的是temp对应的二进制的补码
        if (flag) {
            return str.substring(str.length() - 8);//截取str倒数第8位
        } else {
            return str;
        }
    }
}
