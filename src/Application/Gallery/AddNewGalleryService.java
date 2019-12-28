package Application.Gallery;


import Application.IRepository;

public class AddNewGalleryService {
    private IRepository repository;

    public AddNewGalleryService(IRepository repository) {
        this.repository = repository;
    }

    public void invoke(String path){
        this.repository.fetch().setNewGalleryPath(path);
    }
}
