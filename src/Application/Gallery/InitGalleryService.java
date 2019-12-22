package Application.Gallery;

import java.io.IOException;

public class InitGalleryService {
    private IGalleryRepository repository;

    public InitGalleryService(IGalleryRepository repository) {
        this.repository = repository;
    }

    public void invoke(String path) throws IOException {
        this.repository.init(path);
    }
}
