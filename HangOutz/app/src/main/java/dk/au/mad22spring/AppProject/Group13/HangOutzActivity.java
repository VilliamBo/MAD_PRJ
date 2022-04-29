/*References:
Introduction to read/write and create a data structure in Firebase Realtime Database
Firebase Realtime Database By Example with Android: https://medium.com/@valokafor/firebase-realtime-database-by-example-with-android-1e597819e24b
*/
package dk.au.mad22spring.AppProject.Group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class HangOutzActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    //ui widgets
    public Button btnAddFriend;
    public Button btnWrite;
    public TextView txtCount;
    public EditText edtAddFriendUserID;
    private int counter = 0;

    //local variables
    private Repository repo;
    private User localUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hang_outz);

        repo = Repository.getInstance();

        localUser = new User("1234", "David");

        setupUI();
        //Cast exception if write to DB fails.

        fillDB();
        repo.addUser(localUser);
    }


    private void fillDB() {
        for (int i = 0; i < 10; i++) {
            repo.addUser(new User(""+i, "User"+i));
        }
    }

    private void setupUI() {
        edtAddFriendUserID = findViewById(R.id.edtAddFriendUserId);
        txtCount = findViewById(R.id.txtCount);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendId = edtAddFriendUserID.getText().toString();
                repo.addFriend(localUser.id, friendId);
            }
        });
        btnWrite = findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(view -> {
            counter++;
            ArrayList<String> location = new ArrayList<>();
            localUser.location1=""+(38+counter*2);
            localUser.location2=""+counter;
            repo.setLocation(localUser.id, localUser.location1, localUser.location2);
        });

    }
}