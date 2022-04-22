package comp3021.src.comp3021.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class NoteBook implements java.io.Serializable{
    private static final long serialVersionUID =1L;
    private ArrayList<Folder> folders;
    public NoteBook(){
        folders = new  ArrayList<Folder>();
    }
    public NoteBook(String file){
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try{
            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);
            NoteBook object = (NoteBook) in.readObject();
            this.folders = object.getFolders();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
    public boolean save(String file){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void addFolder(String folderName) {
        // TO DO
        folders.add(new Folder(folderName));
    }

}
