package comp3021.src.comp3021.base;

import java.util.ArrayList;
import java.util.Objects;

public class Folder {
    private ArrayList<Note> notes;
    private String name;
    public Folder (String name){
        this.name = name;
        notes = new ArrayList<Note>();
    }
    public void addNote(Note no){
        notes.add(no);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }
    public String toString(){
        int nText =0;
        int nImage =0;
        for (Note x: notes){
            if (x instanceof TextNote){
                nText +=1;
            }
            if (x instanceof ImageNote){
                nImage+=1;
            }
        }
        return name +":"+nText +":"+nImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(name, folder.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
