package Domain;


import org.json.simple.JSONObject;

import java.util.HashMap;

public class Gallery {
    private String rootGalleryDir = "/home/karol/projects/fitdev/galeria";
    private String newGalleryDir = "/home/karol/projects/fitdev/galeriaN";

    public Gallery(String rootGalleryDir, HashMap<String, String> settings) {
        this.rootGalleryDir = rootGalleryDir;
    }

    public void setNewGalleryPath(String path) {
        this.newGalleryDir = path;
    }

    public String getRootGalleryDir(){
        return this.rootGalleryDir;
    }

    public String getNewGalleryDir() {
        return newGalleryDir;
    }

    public String toJsonString(){
        JSONObject data = new JSONObject();
        data.put("firstName", "Lokesh");
        data.put("lastName", "Gupta");
        data.put("website", "howtodoinjava.com");

        return data.toJSONString();
    }

    public static Gallery fromJson(String path, JSONObject data){
        return new Gallery(path, data);
    }
}
