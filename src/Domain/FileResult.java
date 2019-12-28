package Domain;

public class FileResult {

    private String source;
    private String result;

    public FileResult(String source, String result) {
        this.source = source;
        this.result = result;
    }

    public String getSource() {
        return source;
    }

    public String getResult() {
        return result;
    }
}
