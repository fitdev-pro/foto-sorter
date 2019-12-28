package Application;

import Domain.Gallery;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    Gallery fetch();
    void saveGallerySettings(Gallery gallery) throws IOException;
    void initGallery(String path) throws IOException;
    List<String> getFiles(String path);
}
