/*References:
Introduction to read/write and create a data structure in Firebase Realtime Database
Firebase Realtime Database By Example with Android: https://medium.com/@valokafor/firebase-realtime-database-by-example-with-android-1e597819e24b
*/
package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class HangOutzActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    //ui widgets
    public Button btnAddFriend, btnWrite, btnGoToAddFriend, btnFriendList, btnUpdateUser, btnDeleteUser;
    public TextView txtCount;
    public EditText edtAddFriendUserID, edtDeleteUser;
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

        //fillDB();
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
            localUser.location1=""+(38+counter*2);
            localUser.location2=""+counter;
            repo.setUserLocation(localUser.id, localUser.location1, localUser.location2);
        });

        btnGoToAddFriend = findViewById(R.id.btnGoToAddFriend);
        btnGoToAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HangOutzActivity.this, FriendsAddActivity.class);
                startActivity(i);
            }
        });

        btnFriendList = findViewById(R.id.btnFriendList);
        btnFriendList.setOnClickListener(view -> {
            Intent i = new Intent(HangOutzActivity.this, FriendsListActivity.class);
            startActivity(i);
        });

        btnUpdateUser = findViewById(R.id.btnUpdateUser);
        btnUpdateUser.setOnClickListener(view -> {
            repo.setUserLocation("1234", ""+counter, ""+counter);
            repo.setUserImg("1234", "img"+counter);
            repo.setUserName("1234", "David"+counter);
            counter++;
        });

        edtDeleteUser = findViewById(R.id.edtDeleteUser);

        btnDeleteUser = findViewById(R.id.btnDelete);
        btnDeleteUser.setOnClickListener(view ->{repo.deleteUser(edtDeleteUser.getText().toString());});


    }
}