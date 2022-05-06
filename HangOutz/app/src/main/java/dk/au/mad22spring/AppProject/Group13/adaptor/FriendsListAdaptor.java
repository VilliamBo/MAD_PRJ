
//AUTHORS:
//Villiam Holger Bo - 201907166
//David Vestergaard Kristensen - 201908226
//Jonathan ********** - au*****

//The authors have been working in collaboration on the project.
//Therefore, the handIns for both students are identical.
//This code has been heavily inspired by MAD course:
// Lecture 2 clicker app

package dk.au.mad22spring.AppProject.Group13.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.R;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class FriendsListAdaptor extends RecyclerView.Adapter<FriendsListAdaptor.ViewHolder>{

    private static final String TAG = "FriendsListAdaptor";
    private Context context;
    private ArrayList<User> friends;

    //interface for when a Item is clicked
    public interface IFriendItemClickedListener {
        void onFriendClicked(User friendClicked);
    }

    private IFriendItemClickedListener friendListener;

    public FriendsListAdaptor(Context context, ArrayList<User> friendsList, IFriendItemClickedListener listener) {
        this.context = context;
        friends = friendsList;
        this.friendListener = listener;
    }

    @NonNull
    @Override
    public FriendsListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull FriendsListAdaptor.ViewHolder holder, int position) {

        //checking for available image, else insert default image
        /*
        if((firends.get(position).getStrDrinkThumb() != null) && (!drinks.get(position).getStrDrinkThumb().equals("null")))
        {
            Glide.with(holder.imgDrink.getContext()).load(drinks.get(position).getStrDrinkThumb()).into(holder.imgDrink);
        }
        else
        {
            Log.d(TAG, "onBindViewHolder: no image, using default image instead");
            holder.imgDrink.setImageResource(R.raw.xtrawater);
        }
        */
        User user = friends.get(position);
        holder.txtFriendId.setText(user.id);
        holder.txtFriendName.setText(user.name);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.friendItem.setElevation(10);
        }
    }

    @Override
    public int getItemCount() {
        if(friends == null) {
            return 0;
        }
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        //FriendList item widgets
        public TextView txtFriendName, txtFriendId;
        public CardView friendItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendItem = itemView.findViewById(R.id.friendItem);
            txtFriendName = itemView.findViewById(R.id.txtFriendName);
            txtFriendId = itemView.findViewById(R.id.txtFirendId);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                User friendClicked = friends.get(position);
                friendListener.onFriendClicked(friendClicked);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public void setData(ArrayList<User> friends) { this.friends = friends; }

}
