package Infrastructure.FotoCreationDateReaders;

import Domain.IFotoCreationDateReader;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MetaData implements IFotoCreationDateReader {
    @Override
    public Date getDate(File file) {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException | IOException e) {
            return null;
        }

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if(directory == null) {
            return null;
        }

        return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }
}
