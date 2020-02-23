package Application;

import Domain.*;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MoveService {
    private IFilesRepository filesRepository;
    private IGalleryRepository galleryRepository;
    private Collection<IFotoCreationDateReader> fotoCreationDateReaders;

    public MoveService(IFilesRepository filesRepository, IGalleryRepository galleryRepository, Collection<IFotoCreationDateReader> fotoCreationDateReaders) {

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
                    String resultFilePath = gallery.getRootGalleryDir() + "/foto/" + file.generateDirectoryPathWithDate() + "/" + file.getFileName();
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

    public String moveFiles(Collection<FileResult> files) {
        int i = 0;
        int m = 0;
        int e = 0;
        int b = 0;
        int all = files.size();

        for (FileResult item: files) {
            if(!item.isChecked()) {
                File oldFile = new File(item.getSource());
                File newFile = new File(item.getResult());

                if (!newFile.exists()) {
                    File parentDir = new File(newFile.getParent());
                    boolean parentDirExists = parentDir.exists();

                    if (!parentDirExists) {
                        parentDirExists = parentDir.mkdirs();
                    }

                    if (parentDirExists) {
                        boolean successMove = oldFile.renameTo(newFile);
                        if (successMove) {
                            m++;
                        } else {
                            e++;
                        }
                    } else {
                        e++;
                    }
                } else {
                    b++;
                }
            }else{
                i++;
            }
        }

        return "all:"+all+"/move:"+m+"/error:"+e+"/exists:"+b+"/ignore:"+i;
    }
}
