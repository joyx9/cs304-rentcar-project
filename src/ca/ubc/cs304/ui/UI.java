package ca.ubc.cs304.ui;

public class UI {

    MainDisplay md;

    public UI(){
        md = new MainDisplay();
        md.showDisplay(null);
    }

    public static void main(String[] args){
        new UI();
    }
}
