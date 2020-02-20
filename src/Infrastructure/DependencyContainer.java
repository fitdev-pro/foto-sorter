package Infrastructure;

import Application.*;
import Domain.IFotoCreationDateReader;
import Infrastructure.FotoCreationDateReaders.MetaData;

import java.util.Collection;
import java.util.LinkedList;

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
        return new RenamingService(filesRepository, galleryRepository, getFotoCreationDateReaders());
    }

    public MoveService getMoveService() {

        return new MoveService(filesRepository, galleryRepository, getFotoCreationDateReaders());
    }

    private Collection<IFotoCreationDateReader> getFotoCreationDateReaders(){
        Collection<IFotoCreationDateReader> fotoCreationDateReaders = new LinkedList<IFotoCreationDateReader>();
        fotoCreationDateReaders.add(new MetaData());

        return fotoCreationDateReaders;
    }

    public DuplicatesService getDuplicatesService() {
        return new DuplicatesService(filesRepository, galleryRepository, getFotoCreationDateReaders());
    }

    public DeletingService getDeletingService(){
        return new DeletingService(filesRepository, galleryRepository);
    }
}
