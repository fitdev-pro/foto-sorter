package Application;

import Domain.Gallery;

public interface IGalleryRepository {
    Gallery fetch() throws GalleryNotInitException;
    void initGallery(String path) throws GalleryNotInitException;
}
