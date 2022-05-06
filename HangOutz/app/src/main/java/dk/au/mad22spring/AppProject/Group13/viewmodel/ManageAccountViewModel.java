package dk.au.mad22spring.AppProject.Group13.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad22spring.AppProject.Group13.model.Repository;

public class ManageAccountViewModel extends AndroidViewModel {

    private Repository repository;


    public ManageAccountViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
    }

    public void deleteAccount() {
        repository.deleteAccount(repository.getLoggedInUserID());
    }

    public void updateUser(String name) {
        repository.updateUser(name);
    }
}
