package Infrastructure;

import Application.Gallery.IGalleryRepository;
import Domain.Gallery;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class GalleryRepository implements IGalleryRepository
{
    private Gallery gallery;

    @Override
    public Gallery fetch() {
        return this.gallery;
    }

    @Override
    public void save(Gallery gallery) throws IOException {
        FileWriter file = new FileWriter(this.gallery.getRootGalleryDir()+"/gallerySettings.json");
        file.write(gallery.toJsonString());
        file.flush();
    }

    @Override
    public void init(String path) throws IOException {
        try (FileReader reader = new FileReader(path+"/gallerySettings.json"))
        {
            //Read JSON file
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);

            JSONObject galleryData = (JSONObject) obj;

            this.gallery = Gallery.fromJson(path, galleryData);
        } catch (FileNotFoundException | ParseException e) {
            this.gallery = new Gallery(path, new HashMap<String, String>());
            this.save(gallery);
        }
    }
}
