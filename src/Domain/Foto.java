package Domain;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class Foto {
    private File file;
    private Collection<IFotoCreationDateReader> fotoCreationDateReaders;

    public Foto(String file, Collection<IFotoCreationDateReader> fotoCreationDateReaders) {
        this.file = new File(file);
        this.fotoCreationDateReaders = fotoCreationDateReaders;
    }

    public Foto(String file) {
        this.file = new File(file);
    }

    public boolean isFoto(){
        try {
            String mimetype = Files.probeContentType(file.toPath());
            return mimetype != null && mimetype.split("/")[0].equals("image");
        } catch (Exception e){
            return false;
        }
    }

    public String getDirectoryPath(){
        return file.getParent();
    }

    public String getFileName(){
        return file.getName();
    }

    public String generateFileNameWithDate() throws CorruptedFotoException {
        Date date = getFileCreateDate();
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        String ext = i > 0 ? fileName.substring(i + 1) : "";
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        return dateStr+"."+ext;
    }

    public String generateDirectoryPathWithDate() throws CorruptedFotoException{
        Date date = getFileCreateDate();
        return new SimpleDateFormat("yyyy/MM").format(date);
    }

    private Date getFileCreateDate() throws CorruptedFotoException {
        Date out = null;
        for (IFotoCreationDateReader reader : fotoCreationDateReaders){
            out = reader.getDate(file);

            if(out != null){
                break;
            }
        }

        if(out == null){
            throw new CorruptedFotoException("Corrupted foto image: "+file.getPath());
        }

        return out;
    }
}
