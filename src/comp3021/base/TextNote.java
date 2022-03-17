package comp3021.src.comp3021.base;

import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
    public TextNote(File f){
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }
    private String getTextFromFile(String absolutePath){
        String result = "";


        FileInputStream fis = null;
        ObjectInputStream in = null;
        try{
            fis = new FileInputStream(absolutePath);
            in = new ObjectInputStream(fis);
            TextNote object = (TextNote) in.readObject();
            return object.getContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public void exportTextToFile(String pathFolder){
        if (pathFolder.equals("")){
            pathFolder= ".";
        }
        File file = new File(pathFolder+File.separator+this.getTitle().replaceAll(" ","_")+".txt");
        //FileWriter out1 = new FileWriter(file,false);
        try{
            FileWriter out1 = new FileWriter(file,false);
            //file.createNewFile();
            //FileOutputStream out1 = new FileOutputStream(file);
            //ObjectOutputStream out2 = new ObjectOutputStream(out1);
            //out2.writeObject(this);
            //out2.close();
            out1.write(this.content);
            out1.close();

            //FileWriter test1= new FileWriter(file, false);
            //test1.write(this.getContent());
            //test1.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
