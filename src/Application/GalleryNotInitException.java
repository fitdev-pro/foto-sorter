package Application;


public class GalleryNotInitException extends Exception{
    public GalleryNotInitException(String message, Exception e) {
        super(message, e);
    }
}
