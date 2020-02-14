package Application.Gallery;

import Application.IRepository;
import Application.IService;
import Domain.FileResult;

import java.util.Collection;


public class InitGalleryService implements IService {
    private String path;

    public InitGalleryService(String path) {
        this.path = path;
    }

    public Collection<FileResult> invoke(IRepository repository){
        return repository.initGallery(path);
    }
}
