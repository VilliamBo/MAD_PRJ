package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import dk.au.mad22spring.AppProject.Group13.adaptor.FriendsListAdaptor;
import dk.au.mad22spring.AppProject.Group13.model.Authentication;
import dk.au.mad22spring.AppProject.Group13.model.Repository;
import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.FriendsListViewModel;

public class FriendsListActivity extends AppCompatActivity implements FriendsListAdaptor.IFriendItemClickedListener {

    //testing
    private Button checkUID;

    //Recycler view
    RecyclerView recFriends;
    FriendsListAdaptor adaptor;
    RecyclerView.LayoutManager layoutMan;

    //View model
    private FriendsListViewModel vm;

    //UI widgets
    private Button btnBack, btnAddFriend;
    private EditText edtSearchFriend;

    private String localUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        localUserId = Repository.getInstance().getLoggedInUserID();

        //setup viewModel
        vm = new ViewModelProvider(this).get(FriendsListViewModel.class);

        //setup recycler view
        recFriends = findViewById(R.id.recFriendsList);
        layoutMan = new LinearLayoutManager(this);
        recFriends.setLayoutManager(layoutMan);
        recFriends.setHasFixedSize(true);

        //setup widget
        setupUI();

        //setup adaptor
        adaptor = new FriendsListAdaptor(this, vm.getFriendList().getValue(), FriendsListActivity.this);

        //setup observer
        //Friend Id list observer. When friend id list i  updated a ned user id is added
        //to the friend.child(userid). When his happens the friends list needs to
        //be updated via updateFriendList() using the new friends id's.
        vm.getFriendIdList().observe(this, users -> {vm.updateFriendList();});
        vm.getFriendList().observe(this, users -> {updateAdapter(users);});

        recFriends.setAdapter(adaptor);

        checkUID = findViewById(R.id.checkUID);
        checkUID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CurrentUserID", "UID: " + localUserId);
            }
        });

    }

    @Override
    public void onFriendClicked(String id) {
        Toast.makeText(this, "Friend " + id + " clicked.", Toast.LENGTH_SHORT).show();
    }

    private void updateAdapter(ArrayList<User> friends) {
        adaptor.setData(friends);
        adaptor.notifyDataSetChanged();
    }

    private void setupUI() {
        btnBack = findViewById(R.id.btnBackFirendList);
        //edtSearchFriend = findViewById(R.id.edtSearchFriend);
        //edtSearchFriend.setText("");

        btnBack.setOnClickListener(view -> finish());

        /*
        edtSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = edtSearchFriend.getText().toString();
                vm.search(str);
            }
            @Override
            public void afterTextChanged(Editable editable){}
        });

         */
    }
}