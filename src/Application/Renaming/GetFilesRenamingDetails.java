package Application.Renaming;

import Application.IRepository;
import Domain.FileResult;
import Domain.Gallery;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class GetFilesRenamingDetails {
    private IRepository repository;

    public GetFilesRenamingDetails(IRepository repository) {
        this.repository = repository;
    }

    public Collection<FileResult> invoke() throws IOException {
        Gallery gallery = this.repository.fetch();
        List<String> files = this.repository.getFiles(gallery.getNewGalleryDir());

        Collection<FileResult> out = new LinkedList<>();

        if(files != null){
            for(String fileName : files){
                try {
                    Metadata metadata = ImageMetadataReader.readMetadata(new File(fileName));
                    ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                    if(directory == null) {
                        throw new ImageProcessingException("No date");
                    }
                    Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                    if(date == null) {
                        throw new ImageProcessingException("No date");
                    }
                    out.add(new FileResult(fileName, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)));
                }catch (ImageProcessingException e){
                    out.add(new FileResult(fileName, "no date"));
                }
            }
        }

        return out;
    }
}
