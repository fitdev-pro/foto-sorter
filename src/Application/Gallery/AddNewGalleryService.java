package Application.Gallery;


import Application.IRepository;
import Application.IService;

public class AddNewGalleryService implements IService {
    private String path;

    public AddNewGalleryService(String path) {
        this.path = path;
    }

    public void invoke(IRepository repository){
        repository.fetch().setNewGalleryPath(this.path);
    }
}
