package comp3021.src.comp3021.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Note implements Comparable<Note>, Serializable {
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

    @Override
    public int compareTo(Note o) {
        //More recent is smaller
        if (this.date.after(o.date)){
            return -1;
        }
        else if (this.date.equals(o.date)){
            return 0;
        }
        return 1;
    }
    public String toString(){
        return date.toString() + "\t" +title;
    }
}
