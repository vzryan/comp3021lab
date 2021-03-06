package comp3021.src.comp3021.base;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.*;

public class Folder implements Comparable<Folder>, Serializable {
    private ArrayList<Note> notes;
    private String name;
    public Folder (String name){
        this.name = name;
        notes = new ArrayList<Note>();
    }
    public void addNote(Note no){
        notes.add(no);
    }
    public void removeNotes(String title){
        notes = (ArrayList<Note>) notes.stream().filter(x->!x.getTitle().equals(title)).toList();
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

    @Override
    public int compareTo(Folder o) {
        int x =name.compareTo(o.name);
        if (x==0){
            return 0;
        }
        else if (x>0){
            return 1;
        }
        return -1;
    }
    public void sortNotes(){
        Collections.sort(notes);
    }
    public List<Note> searchNotes (String keywords) {
        keywords = keywords.toLowerCase(Locale.ROOT);
        String[] temp = keywords.split(" ");
        if (temp.length <= 1) {
            //System.out.println("DLLM");
            List<Note> result = new ArrayList<>();
            for ( Note y : notes){
                if (y instanceof ImageNote) {
                    if (y.getTitle().toLowerCase(Locale.ROOT).contains(temp[0])) {
                        result.add(y);
                    }
                } else if (y instanceof TextNote) {
                    boolean existtitletext = false;
                    if (y.getTitle().toLowerCase(Locale.ROOT).contains(temp[0])) {
                        existtitletext = true;
                    } else if (((TextNote) y).getContent().toLowerCase(Locale.ROOT).contains(temp[0])) {
                        existtitletext = true;
                    }
                    if (existtitletext) {
                        result.add(y);
                    }
                }
            }
            return result;
        } else {
            List<String> andcondition = new ArrayList<String>();
            for (int i = 0; i <= temp.length - 1; i++) {
                if (i == 0) {
                    if (!temp[i + 1].equals("or")) {
                        if (!temp[i].equals("or")) {
                            andcondition.add(temp[i]);
                        }
                    }
                }
                if (i == temp.length - 1) {
                    if (!temp[i - 1].equals("or")) {
                        if (!temp[i].equals("or")) {
                            andcondition.add(temp[i]);
                        }
                    }
                }
                if ((i >= 1) && (i < temp.length - 1)) {
                    if (!temp[i - 1].equals("or")) {
                        if (!temp[i + 1].equals("or")) {
                            if (!temp[i].equals("or")) {
                                andcondition.add(temp[i]);
                            }
                        }
                    }
                }
            }
            //System.out.println("And condtions");
            //System.out.println(andcondition);
            List<List<String>> orcondition = new ArrayList<List<String>>();
            List<String> tempor = new ArrayList<String>();
            for (int i = 0; i <= temp.length - 1; i++) {
                if ((i >= 1) & (i < temp.length - 1)) {
                    if (!temp[i - 1].equals("or") && (temp[i + 1].equals("or"))) {
                        tempor = new ArrayList<String>();
                        tempor.add(temp[i]);
                    }
                    if (temp[i - 1].equals("or") && (temp[i + 1].equals("or"))) {
                        tempor.add(temp[i]);
                    }
                    if (temp[i - 1].equals("or") && (!temp[i + 1].equals("or"))) {
                        tempor.add(temp[i]);
                        orcondition.add(tempor);
                        tempor = null;
                    }
                }
                if ((i >= 1) & (i == temp.length - 1)) {
                    if (temp[i - 1].equals("or")) {
                        tempor.add(temp[i]);
                        orcondition.add(tempor);
                        tempor = null;
                    }
                }
                if ((i == 0) && (i < temp.length - 1)) {
                    if (temp[i + 1].equals("or")) {
                        tempor = new ArrayList<String>();
                        tempor.add(temp[i]);
                    }
                }
            }
            //System.out.println("Or condtions");
           // System.out.println(orcondition);

            List<Note> result = new ArrayList<>();
            for (Note y : notes) {
                boolean allexist = true;
                for (String x : andcondition) {
                    if (y instanceof ImageNote) {
                        if (!y.getTitle().toLowerCase(Locale.ROOT).contains(x)) {
                            allexist = false;
                        }
                    } else if (y instanceof TextNote) {
                        boolean existtitletext = false;
                        if (y.getTitle().toLowerCase(Locale.ROOT).contains(x)) {
                            existtitletext = true;
                        } else if (((TextNote) y).getContent().toLowerCase(Locale.ROOT).contains(x)) {
                            existtitletext = true;
                        }
                        if (!existtitletext) {
                            allexist = false;
                        }
                    }

                }
                boolean allexistor = true;
                for (List<String> q : orcondition) {
                    //Check each or compound if all have
                    //Check each string in or if either exist
                    boolean allexistin = false;
                    for (String p : q) {
                        if (y instanceof ImageNote) {
                            if (y.getTitle().toLowerCase(Locale.ROOT).contains(p)) {
                                allexistin = true;
                            }
                        }
                        if (y instanceof TextNote) {
                            if (((TextNote) y).getContent().toLowerCase(Locale.ROOT).contains(p)) {
                                allexistin = true;
                            }
                            if (((TextNote) y).getTitle().toLowerCase(Locale.ROOT).contains(p)) {
                                allexistin = true;
                            }

                        }
                    }
                    if (!allexistin) {
                        allexistor = false;
                    }
                }
                if (allexistor && allexist) {
                    result.add(y);
                }
            }

            //System.out.println(result);
            return result;
        }
    }
}
