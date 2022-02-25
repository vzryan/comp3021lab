package comp3021.src.comp3021.base;

import java.util.ArrayList;

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
}
