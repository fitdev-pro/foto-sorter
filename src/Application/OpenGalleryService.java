package Application;

import Domain.Gallery;

public class OpenGalleryService {
    public static Gallery dispath(String path, String settings){
        return new Gallery(path);
    }
}
