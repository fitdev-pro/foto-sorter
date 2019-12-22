package Application;

import Domain.Gallery;

public class InitGalleryService {
    public static Gallery dispath(String path){
        return new Gallery(path);
    }
}
