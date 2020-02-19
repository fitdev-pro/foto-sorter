package Application;

import Domain.FileResult;
import Domain.Gallery;
import Infrastructure.GalleryNotInitException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DeletingService {
    private IFilesRepository filesRepository;
    private IGalleryRepository galleryRepository;

    public DeletingService(IFilesRepository filesRepository, IGalleryRepository galleryRepository) {

        this.filesRepository = filesRepository;
        this.galleryRepository = galleryRepository;
    }

    public Collection<FileResult> fetchNewFiles() throws GalleryNotInitException {
        Gallery gallery = galleryRepository.fetch();
        List<String> files = filesRepository.getFiles(gallery.getNewGalleryDir());

        Collection<FileResult> out = new LinkedList<>();

        if(files != null){
            for(String filePath : files){
                out.add(new FileResult(filePath,""));
            }
        }

        return out;
    }
}
