package comp3021.src.comp3021.base;

public class TextNote extends Note{
    private String content;
    public TextNote (String title){
        super(title);
    }
    public String getContent(){return content;};
    public TextNote (String title, String content){
        super(title);
        this.content = content;
    }
}
