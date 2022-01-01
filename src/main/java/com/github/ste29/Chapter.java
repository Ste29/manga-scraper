package com.github.ste29;

public class Chapter {

    public final String url;
    public final String name;
    public final int number;

    public Chapter(String url, String name) {
        this.url = url;
        this.name = name;
        this.number = getNumber();
    }

    public String getChapUrl(){
        return name.replace(": ", " ").replace(":", " ").replace(" ", "_");
    }

    public int getNumber() {

        String tmpName = name + " ";
        String[] n = tmpName.split(""); //array of strings
        StringBuffer f = new StringBuffer(); // buffer to store numbers

        for (int i = 0; i < n.length; i++) {
            if((n[i].matches("[0-9]+"))) {// validating numbers
                f.append(n[i]); //appending
            }else {
                //parsing to int and returning value
                return Integer.parseInt(f.toString());
            }
        }
        return 0;
    }

}