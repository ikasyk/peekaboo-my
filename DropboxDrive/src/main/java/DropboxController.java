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

public class DropboxController implements FileManager {
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

    /**
     * Get access token for current user account.
     * @return access token.
     * @throws IOException System.in exception.
     * @throws DbxException DbxWebAuthNoRedirect finish exception.
     */
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

    /**
     * Sets session token.
     * @param accessToken the unique session string, is not expired.
     */
    public void setToken(String accessToken) {
        client = new DbxClient(config, accessToken);
    }

    /**
     * Uploads file on Dropbox server.
     * @param inputFile current file for upload.
     * @param filePath file location on Dropbox server.
     * @throws IOException when file input error.
     * @throws DbxException client's upload exception.
     */
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

    /**
     * Downloads file from Dropbox server.
     * @param outputFile file which will download.
     * @param filePath file location on server.
     * @throws IOException when file output error.
     * @throws DbxException client's getFile exception.
     */
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

    /**
     * Get a list of file on current path.
     * @param path server folder location.
     * @return the list {file name: file entry}
     * @throws DbxException client's getMetadataWithChildren exception.
     */
    public Map<String, Object> getFileList(String path)
            throws DbxException {
        Map<String, Object> fileList = new HashMap<String, Object>();
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

    /**
     * Removes file from server.
     * @param path the file location.
     * @throws DbxException client's delete exception.
     */
    public void remove(String path) throws DbxException {
        client.delete(path);
    }

    /**
     * Moves file from one location to another.
     * @param fromPath path of file.
     * @param toPath path where file will be moved.
     * @return the entry with new file.
     * @throws DbxException when client's move exception.
     */
    public DbxEntry move(String fromPath, String toPath) throws DbxException {
        return client.move(fromPath, toPath);
    }

    /**
     * Copies file from one location to another
     * @param fromPath path of file.
     * @param toPath path where file will be copied.
     * @return the entry with new file.
     * @throws DbxException when client's copy exception.
     */
    public DbxEntry copy(String fromPath, String toPath) throws DbxException {
        return client.copy(fromPath, toPath);
    }

    /**
     * Creates a folder on server.
     * @param path location of new folder.
     * @return the entry with new folder.
     * @throws DbxException when client's createFolder exception.
     */
    public DbxEntry.Folder createFolder(String path) throws DbxException {
        return client.createFolder(path);
    }
}
