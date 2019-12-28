package Infrastructure;

import Application.IRepository;
import Domain.FileResult;
import Domain.Gallery;
import UserInterface.Components.Dialogs.ExceptionDialog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GalleryRepository implements IRepository
{
    private Gallery gallery;

    @Override
    public Gallery fetch() {
        return this.gallery;
    }

    @Override
    public void saveGallerySettings(Gallery gallery) throws IOException {
        FileWriter file = new FileWriter(this.gallery.getRootGalleryDir()+"/gallerySettings.json");
        file.write(gallery.toJsonString());
        file.flush();
    }

    @Override
    public void initGallery(String path) throws IOException {
        try (FileReader reader = new FileReader(path+"/gallerySettings.json"))
        {
            //Read JSON file
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);

            JSONObject galleryData = (JSONObject) obj;

            this.gallery = Gallery.fromJson(path, galleryData);
        } catch (FileNotFoundException | ParseException e) {
            this.gallery = new Gallery(path, new HashMap<String, String>());
            this.saveGallerySettings(gallery);
        }
    }

    @Override
    public List<String> getFiles(String path) {
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            ExceptionDialog.show(e);
            return null;
        }
    }
}
