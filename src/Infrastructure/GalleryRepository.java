package Infrastructure;

import Application.IGalleryRepository;
import Domain.Gallery;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class GalleryRepository implements IGalleryRepository
{
    private Gallery gallery;

    @Override
    public Gallery fetch() throws GalleryNotInitException {
        if(gallery == null){
            throw new GalleryNotInitException("Failed to init gallery settings", new NullPointerException());
        }
        return this.gallery;
    }

    private void saveGallerySettings(Gallery gallery) throws GalleryNotInitException {
        try {
            FileWriter file = new FileWriter(this.gallery.getRootGalleryDir() + "/gallerySettings.json");
            file.write(gallery.toJsonString());
            file.flush();
        } catch (IOException e){
            throw new GalleryNotInitException("Failed to init gallery settings", e);
        }
    }

    @Override
    public void initGallery(String path) throws GalleryNotInitException{
        try (FileReader reader = new FileReader(path+"/gallerySettings.json"))
        {
            //Read JSON file
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);

            JSONObject galleryData = (JSONObject) obj;

            this.gallery = Gallery.fromJson(path, galleryData);
        } catch (IOException | ParseException e) {
            this.gallery = new Gallery(path, new HashMap<String, String>());
            this.saveGallerySettings(gallery);
        }
    }
}
