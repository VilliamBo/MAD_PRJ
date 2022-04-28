package dk.au.mad22spring.AppProject.Group13.model;

public class Repository {

    private static Repository instance;
    private FirebaseDB database;

    private Repository(){
        database = new FirebaseDB();
    }

    //Singleton pattern
    public static Repository getInstance(){
        if (instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public void addUser(User user){
        if(user != null){
            database.addUser(user);
        }
    }

    public void deleteUser(String userId){

    }
}
