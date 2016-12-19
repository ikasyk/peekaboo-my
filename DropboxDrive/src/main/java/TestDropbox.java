import com.dropbox.core.*;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestDropbox {
    public static void main(String[] args) {
        // Get your app key and secret from the Dropbox developers website.
        DropboxController dc = new DropboxController();
        try {
//            dc.setToken(dc.getAccess());
            dc.setToken("1maDa6DnBTAAAAAAAAAADX0-m3UyqRp0wKTAR62-Bt7oqg_9LUAep8T7UnvISL8V");
//            dc.upload(new File("./a.txt"), "/igor/my/folder/tester.txt");
//            dc.download(new File("downloaded.txt"), "/test.txt");
            Map<String, Object> list = dc.getFileList("/");
            for (Map.Entry<String, Object> f : list.entrySet()) {
                System.out.println(f.getKey());
            }
//            dc.move("/test.txt", "/mytesttest.txt");
//            dc.createFolder("/igor/dev");
//            dc.copy("/test (3).txt", "/igor/my.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}