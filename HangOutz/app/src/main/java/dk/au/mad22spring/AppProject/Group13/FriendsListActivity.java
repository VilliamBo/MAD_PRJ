package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
    private Button btnBack, btnGoToAddFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

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

    }

    @Override
    public void onFriendClicked(User friendClicked) {
        showFriendDialog(friendClicked);
    }

    private void showFriendDialog(User friendClicked) {
        final Dialog dialog = new Dialog(FriendsListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.friend_clicked_dialog);
        TextView name = dialog.findViewById(R.id.txtFriendDialogName);
        name.setText(friendClicked.name);

        Button btnDelete = dialog.findViewById(R.id.btnFriendDialogDelete);

        btnDelete.setOnClickListener(v -> {
            vm.removeFriend(friendClicked);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateAdapter(ArrayList<User> friends) {
        adaptor.setData(friends);
        adaptor.notifyDataSetChanged();
    }

    private void setupUI() {

        btnBack = findViewById(R.id.btnBackFirendList);

        btnBack.setOnClickListener(view -> finish());

        btnGoToAddFriends = findViewById(R.id.btnGoToAddFriends);
        btnGoToAddFriends.setOnClickListener(view -> {
            Intent i = new Intent(FriendsListActivity.this, FriendsAddActivity.class);
            startActivity(i);
        });

    }
}