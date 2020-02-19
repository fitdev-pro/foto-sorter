package Application;

import Domain.Gallery;
import Infrastructure.GalleryNotInitException;

import java.io.IOException;
import java.util.List;

public interface IGalleryRepository {
    Gallery fetch() throws GalleryNotInitException;
    void initGallery(String path) throws GalleryNotInitException;
}
