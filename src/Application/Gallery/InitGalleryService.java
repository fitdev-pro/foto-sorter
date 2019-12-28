package Application.Gallery;

import Application.IRepository;

import java.io.IOException;

public class InitGalleryService {
    private IRepository repository;

    public InitGalleryService(IRepository repository) {
        this.repository = repository;
    }

    public void invoke(String path) throws IOException {
        this.repository.initGallery(path);
    }
}
