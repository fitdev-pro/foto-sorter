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

    public Collection<FileResult> invoke() {
        Gallery gallery = this.repository.fetch();
        List<String> files = this.repository.getFiles(gallery.getNewGalleryDir());

        Collection<FileResult> out = new LinkedList<>();

        if(files != null){
            for(String filePath : files){
                File file = new File(filePath);
                Metadata metadata = null;
                try {
                    metadata = ImageMetadataReader.readMetadata(file);
                } catch (ImageProcessingException|IOException e) {
                    continue;
                }

                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if(directory == null) {
                    continue;
                }
                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                if(date == null) {
                    continue;
                }

                String resultFilePath =  generateResultFilePath(file.getParent(), file.getName(), date);

                if(!filePath.equals(resultFilePath)){
                    out.add(new FileResult(filePath,resultFilePath));
                }
            }
        }

        return out;
    }

    private String generateResultFilePath(final String dirName, final String fileName, final Date date)
    {
        int i = fileName.lastIndexOf('.');
        String ext = i > 0 ? fileName.substring(i + 1) : "";
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        return dirName+"/"+dateStr+"."+ext;
    }
}
