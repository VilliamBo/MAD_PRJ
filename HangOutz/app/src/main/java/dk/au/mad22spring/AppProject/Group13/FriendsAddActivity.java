package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import dk.au.mad22spring.AppProject.Group13.adaptor.FriendsListAdaptor;
import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.FriendsAddViewModel;

public class FriendsAddActivity extends AppCompatActivity implements FriendsListAdaptor.IFriendItemClickedListener {

    //Recycler view
    RecyclerView recFriends;
    FriendsListAdaptor adaptor;
    RecyclerView.LayoutManager layoutMan;

    //View model
    private FriendsAddViewModel vm;

    //UI widgets
    private Button btnBack, btnAddFriend;
    private EditText edtSearchUser;

    private String localUserId = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_add);

        //setup viewModel
        vm = new ViewModelProvider(this).get(FriendsAddViewModel.class);

        //setup recycler view
        recFriends = findViewById(R.id.recFriendsList);
        layoutMan = new LinearLayoutManager(this);
        recFriends.setLayoutManager(layoutMan);
        recFriends.setHasFixedSize(true);

        //setup widget
        setupUI();

        //setup adaptor
        adaptor = new FriendsListAdaptor(this, vm.getUserList().getValue(), this);

        //setup observer
        vm.getUserList().observe(this, users -> {updateAdapter(users);});

        recFriends.setAdapter(adaptor);
    }

    @Override
    public void onFriendClicked(String id) {
        //Toast.makeText(this, "friend " + id + " clicked.", Toast.LENGTH_SHORT).show();
        User u = vm.getUser(id);
        if(u == null){
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "friends with" + u.name , Toast.LENGTH_SHORT).show();
            vm.addFriend(localUserId, u.id);
        }
    }

    private void updateAdapter(ArrayList<User> friends) {
        adaptor.setData(friends);
        adaptor.notifyDataSetChanged();
    }

    private void setupUI() {
        btnBack = findViewById(R.id.btnBack);
        btnAddFriend = findViewById(R.id.btnAddFriend);

        edtSearchUser = findViewById(R.id.edtSearchUser);
        edtSearchUser.setText("");

        btnBack.setOnClickListener(view -> finish());

        edtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = edtSearchUser.getText().toString();
                vm.search(str);
            }
            @Override
            public void afterTextChanged(Editable editable){}
        });
    }

}