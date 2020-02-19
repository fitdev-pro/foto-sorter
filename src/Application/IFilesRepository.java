package Application;

import java.util.List;

public interface IFilesRepository {
    List<String> getFiles(String path);
}
