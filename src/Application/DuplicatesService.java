package Application;

import Domain.*;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DuplicatesService {
    private IFilesRepository filesRepository;
    private IGalleryRepository galleryRepository;
    private Collection<IFotoCreationDateReader> fotoCreationDateReaders;

    public DuplicatesService(IFilesRepository filesRepository, IGalleryRepository galleryRepository, Collection<IFotoCreationDateReader> fotoCreationDateReaders) {
        this.filesRepository = filesRepository;
        this.galleryRepository = galleryRepository;
        this.fotoCreationDateReaders = fotoCreationDateReaders;
    }

    public Collection<FileResult> fetchNewFiles() throws GalleryNotInitException {
        Gallery gallery = galleryRepository.fetch();
        List<String> files = filesRepository.getFiles(gallery.getNewGalleryDir());

        Collection<FileResult> out = new LinkedList<>();

        if(files != null){
            for(String filePath : files){
                Foto file = new Foto(filePath, fotoCreationDateReaders);

                try {
                    String resultFilePath = gallery.getRootGalleryDir() + "/" + file.generateDirectoryPathWithDate() + "/" + file.getFileName();
                    File newFile = new File(resultFilePath);

                    if (!newFile.exists()) {
                        out.add(new FileResult(filePath, resultFilePath));
                    }
                }catch (CorruptedFotoException e){
                    continue;
                }
            }
        }

        return out;
    }

    public String deleteDuplicates(Collection<FileResult> files){
        return "";
    }
}
