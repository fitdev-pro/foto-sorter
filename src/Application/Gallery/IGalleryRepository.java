package Application.Gallery;

import Domain.Gallery;

import java.io.IOException;

public interface IGalleryRepository {
    Gallery fetch();
    void save(Gallery gallery) throws IOException;
    void init(String path) throws IOException;
}
