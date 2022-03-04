package comp3021.src.comp3021.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook {
    private ArrayList<Folder> folders;
    public NoteBook(){
        folders = new  ArrayList<Folder>();
    }
    public boolean createTextNote(String foldername,String title){
        TextNote temp = new TextNote(title);
        return insertNote(foldername,temp);
    }
    public boolean createImageNote(String foldername,String title){
        ImageNote temp = new ImageNote(title);
        return insertNote(foldername,temp);
    }
    public ArrayList<Folder>getFolders(){
        return folders;
    }
    public boolean insertNote(String folderName,Note note){
        Folder dest=null;
        for (Folder x: folders){
            if (x.getName().equals(folderName)){
                dest = x;
            }
        }
        if (dest==null){
            dest = new Folder(folderName);
            folders.add(dest);
        }
        for (Note k : dest.getNotes()){
            if (k.equals(note)){
                System.out.println("Creating note "+note.getTitle()+" under folder "+folderName+" failed");
                return false;
            }
        }
        dest.addNote(note);
        return true;
    }
    public void sortFolders(){
        Collections.sort(folders);
    }
    public boolean createTextNote(String foldername,String title,String content){
        TextNote temp = new TextNote(title,content);
        return insertNote(foldername,temp);
    }
    public List<Note> searchNotes(String keywords){
        List<Note> temp = new ArrayList<>();
        for (Folder x: folders){
            temp.addAll(x.searchNotes(keywords));
        }
        return temp;
    }
}
