import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Project DropboxDrive
 * Created by igor, 16.12.16 17:56
 */
public interface FileManager {
    void upload(File inputFile, String filePath) throws Exception;
    void download(File inputFile, String filePath) throws Exception;
    void remove(String path) throws Exception;
    Map<String, Object> getFileList(String path) throws Exception;
    Object move(String fromPath, String toPath) throws Exception;
    Object copy(String fromPAth, String toPath) throws Exception;
    Object createFolder(String path) throws Exception;
}
