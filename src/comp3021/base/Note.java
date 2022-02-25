package comp3021.src.comp3021.base;

import java.util.Date;
import java.util.Objects;

public class Note {
    private Date date;
    private String title;
    public Note(String title){
        this.title = title;
        date = new Date(System.currentTimeMillis());
    }
    public Date getDate() {
        return date;
    }
    public String getTitle(){
        return title;
    }

    public boolean equals(Note o) {
        return title.equals(o.title);
    }
}
