package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.adaptor.FriendsListAdaptor;
import dk.au.mad22spring.AppProject.Group13.model.User;
import dk.au.mad22spring.AppProject.Group13.viewmodel.FriendsListViewModel;

public class FriendsListActivity extends AppCompatActivity implements FriendsListAdaptor.IFriendItemClickedListener {


    //Recycler view
    RecyclerView recFriends;
    FriendsListAdaptor adaptor;
    RecyclerView.LayoutManager layoutMan;

    //View model
    private FriendsListViewModel vm;

    //UI widgets
    private Button btnBack, btnAddFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //setup viewModel
        vm = new ViewModelProvider(this).get(FriendsListViewModel.class);

        //setup observer
        vm.getFriends("1234").observe(this, friends -> { updateAdapter(friends); });

        //setup widget
        setupUI();

        //setup recycler view
        recFriends = findViewById(R.id.recFirends);
        layoutMan = new LinearLayoutManager(this);
        recFriends.setLayoutManager(layoutMan);
        recFriends.setHasFixedSize(true);

        //setup adaptor
        adaptor = new FriendsListAdaptor(vm.getFriends("1234").getValue(), this);
        recFriends.setAdapter(adaptor);


    }

    @Override
    public void onFriendClicked(String id) {
        Toast.makeText(this, "friend " + id + " clicked.", Toast.LENGTH_SHORT).show();
    }

    private void updateAdapter(ArrayList<User> friends) {
        adaptor.setData(friends);
        adaptor.notifyDataSetChanged();
    }

    private void setupUI() {
        btnBack = findViewById(R.id.btnBack);
        btnAddFriend = findViewById(R.id.btnAddFriend);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}