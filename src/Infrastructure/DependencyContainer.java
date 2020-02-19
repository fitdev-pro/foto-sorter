package Infrastructure;

import Application.*;

public class DependencyContainer {
    private static DependencyContainer instance;

    private IGalleryRepository galleryRepository;
    private IFilesRepository filesRepository;

    public static DependencyContainer getInstance(){
        if(instance == null){
            instance = new DependencyContainer();
        }

        return instance;
    }

    private DependencyContainer() {
        this.galleryRepository = new GalleryRepository();
        this.filesRepository = new FilesRepository();
    }

    public GalleryService getGalleryService() {
        return new GalleryService(galleryRepository);
    }

    public RenamingService getRenamingService() {
        return new RenamingService(filesRepository, galleryRepository);
    }

    public DeletingService getDeletingService(){
        return new DeletingService(filesRepository, galleryRepository);
    }
}
