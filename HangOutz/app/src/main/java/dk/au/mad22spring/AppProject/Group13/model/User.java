package dk.au.mad22spring.AppProject.Group13.model;

import java.util.List;

public class User {
    public String id;
    public String name;
    public String location1;
    public String location2;
    public User(String Id, String Name){
        id = Id;
        name = Name;
        location1 = "unknown";
        location2 = "unknown";
    }
}
