import java.io.Serial;
import java.io.Serializable;

public class FileInformationRelate implements Serializable {
    //源文件需要的一些信息，包括是否为空文件，文件密码，文件父路径，文件纯名，文件类型
    @Serial
   private static final long serialVersionUID=1L;
   public boolean isempty;
   public String parentpath;
   public String filename;
   public String filetype;
   public FileInformationRelate(String parentpath,String filename,String filetype){
       this.filename=filename;
       this.filetype=filetype;
       this.parentpath=parentpath;
   }
}
