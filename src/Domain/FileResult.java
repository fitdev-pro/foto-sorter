package Domain;

public class FileResult {

    private String source;
    private String result;
    private Boolean checked = false;

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

    public boolean isChecked(){
        return checked;
    }

    public void setResult(String result){
        this.result = result;
    }

    public void triggerCheck() {
        if(isChecked()){
            checked = false;
        }else {
            checked = true;
        }
    }
}
