package Application;

import Domain.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                    String resultFilePath = gallery.getRootGalleryDir() + "/foto/" + file.generateDirectoryPathWithDate() + "/" + file.getFileName();
                    File newFile = new File(resultFilePath);

                    if (newFile.exists()) {
                        FileResult fileResult = new FileResult(filePath, resultFilePath);

                        if(!checkMD5(fileResult)){
                            fileResult.triggerCheck();
                        }

                        out.add(fileResult);
                    }
                }catch (CorruptedFotoException e){
                    continue;
                }
            }
        }

        return out;
    }

    public boolean checkMD5(FileResult fileResult){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] sourceHash = md.digest(Files.readAllBytes(Paths.get(fileResult.getSource())));
            byte[] resultHash = md.digest(Files.readAllBytes(Paths.get(fileResult.getResult())));

            return convertHash(sourceHash).equalsIgnoreCase(convertHash(resultHash));
        } catch (IOException | NoSuchAlgorithmException e) {
            return false;
        }
    }

    private String convertHash(byte[] hash){
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< hash.length ;i++)
        {
            sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public String deleteDuplicates(Collection<FileResult> files){
        int i = 0;
        int all = 0;

        for (FileResult item: files) {
            if(!item.isChecked()){
                all++;
                File file = new File(item.getSource());

                if (file.exists()) {
                    boolean success = file.delete();
                    if (success) {
                        i++;
                    }
                }
            }
        }

        return i+"/"+all;
    }
}
