/**
 * Project DropboxDrive
 * Created by igor, 12.12.16 15:55
 */

import com.dropbox.core.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DropboxController {
    public static final String APP_KEY = "9q7jfw7270v9x38";
    public static final String APP_SECRET = "dt2w6bue9hqt37q";

    private DbxAppInfo appInfo;
    private DbxRequestConfig config;

    private DbxClient client = null;

    public DropboxController(String appKey, String appSecret) {
        appInfo = new DbxAppInfo(appKey, appSecret);
        config = new DbxRequestConfig("Peekaboo/1.0", Locale.getDefault().toString());
    }

    public DropboxController() {
        this(APP_KEY, APP_SECRET);
    }

    public String getAccess() throws IOException, DbxException {
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        String authorizeUrl = webAuth.start();
        System.out.println("Access link: " + authorizeUrl + "\n Enter code here: ");

        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        DbxAuthFinish authFinish = webAuth.finish(code);

        String accessToken = authFinish.accessToken;
        System.out.println("Dropbox access token is: " + accessToken);

        return accessToken;
    }

    public void setToken(String accessToken) {
        client = new DbxClient(config, accessToken);
    }

    public void upload(File inputFile, String filePath)
            throws IOException, DbxException {
        if (client != null) {
            FileInputStream inputStream = new FileInputStream(inputFile);
            try {
                DbxEntry.File file = client.uploadFile(filePath, DbxWriteMode.add(), inputFile.length(), inputStream);
                System.out.println("Uploaded: " + file.toString());
            } finally {
                inputStream.close();
            }
        }
    }

    public void download(File outputFile, String filePath)
            throws IOException, DbxException {
        if (client != null) {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            try {
                DbxEntry.File file = client.getFile(filePath, null, outputStream);
                System.out.println("Downloaded: " + file.toString());
            } finally {
                outputStream.close();
            }
        }
    }

    public Map<String, DbxEntry.File> getFileList(String path)
            throws DbxException {
        Map<String, DbxEntry.File> fileList = new HashMap<String, DbxEntry.File>();
        if (client != null) {
            DbxEntry.WithChildren list = client.getMetadataWithChildren(path);
            for (DbxEntry child : list.children) {
                if (child.isFile()) {
                    fileList.put(child.name, child.asFile());
                }
            }
        }
        return fileList;
    }

    public void remove(String path) throws DbxException {
        client.delete(path);
    }

    public DbxEntry move(String fromPath, String toPath) throws DbxException {
        return client.move(fromPath, toPath);
    }

    public DbxEntry copy(String fromPath, String toPath) throws DbxException {
        return client.copy(fromPath, toPath);
    }

    public DbxEntry.Folder createFolder(String path) throws DbxException {
        return client.createFolder(path);
    }
}
