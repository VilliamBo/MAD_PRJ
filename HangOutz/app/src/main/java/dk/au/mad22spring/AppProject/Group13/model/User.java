package dk.au.mad22spring.AppProject.Group13.model;

import java.util.List;

public class User {
    public String id = "unknown";
    public String name = "unknown";
    public String location1 = "unknown";
    public String location2 = "unknown";
    public String email = "unknown";
    public String imgUrl = "unknown";
    public Boolean active = false;
    public String activity = "unknown";
    public int energy = 0;

    public User(){}

    public User(String Id, String Name){
        id = Id;
        name = Name;
        location1 = "unknown";
        location2 = "unknown";
        email = Id + "@gmail.com";
        imgUrl = "www.dr.dk";

    }

    public User(String Id, String Name, String Email, String ImgUrl){
        id = Id;
        name = Name;
        location1 = "unknown";
        location2 = "unknown";
        email = Email;
        imgUrl = ImgUrl;
        active = false;
        activity = "";
        energy = 0;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}