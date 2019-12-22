package Application.Gallery;


public class AddNewGalleryService {
    private IGalleryRepository repository;

    public AddNewGalleryService(IGalleryRepository repository) {
        this.repository = repository;
    }

    public void invoke(String path){
        this.repository.fetch().setNewGalleryPath(path);
    }
}
