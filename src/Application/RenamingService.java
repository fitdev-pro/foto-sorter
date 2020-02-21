package Application;

import Domain.*;

import java.io.File;
import java.util.*;


public class RenamingService {
    private IFilesRepository filesRepository;
    private IGalleryRepository galleryRepository;
    private Collection<IFotoCreationDateReader> fotoCreationDateReaders;

    public RenamingService(IFilesRepository filesRepository, IGalleryRepository galleryRepository, Collection<IFotoCreationDateReader> fotoCreationDateReaders) {

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

                try{
                    if(file.isFoto()) {
                        String resultFilePath = file.getDirectoryPath() + "/" + file.generateFileNameWithDate();
                        if (!filePath.equals(resultFilePath)) {
                            out.add(new FileResult(filePath, resultFilePath));
                        }
                    }
                }catch (CorruptedFotoException e){
                    continue;
                }
            }
        }

        return out;
    }

    public String renameFiles(Collection<FileResult> files){
        int i = 0;
        int e = 0;
        int b = 0;
        int all = files.size();

        for (FileResult item: files) {
            File oldFile = new File(item.getSource());
            File newFile = new File(item.getResult());

            int fileNumber = 1;
            int pos = newFile.getPath().length() - 4;
            while (newFile.exists()){
                String str = newFile.getPath();
                str = str.substring(0, pos) + fileNumber + str.substring(pos + 1);
                newFile = new File(str);
                fileNumber++;
            }

            if (!newFile.exists()) {
                boolean success = oldFile.renameTo(newFile);
                if (success) {
                    i++;
                }else{
                    b++;
                }
            }else{
                e++;
            }
        }

        return "all:"+all+"/renamed:"+i+"/error:"+b+"/exists:"+e;
    }
}
