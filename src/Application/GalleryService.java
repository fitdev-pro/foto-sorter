package Application;

import Infrastructure.GalleryNotInitException;


public class GalleryService {
    private IGalleryRepository repository;

    public GalleryService(IGalleryRepository repository) {
        this.repository = repository;
    }

    public void init(String path) throws GalleryNotInitException {
        repository.initGallery(path);
    }

    public void setNewGalleryPath(String path) throws GalleryNotInitException{
        repository.fetch().setNewGalleryPath(path);
    }
}
