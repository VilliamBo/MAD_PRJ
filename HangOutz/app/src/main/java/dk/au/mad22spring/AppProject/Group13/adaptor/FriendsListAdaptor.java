
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dk.au.mad22spring.AppProject.Group13.R;
import dk.au.mad22spring.AppProject.Group13.model.User;

public class FriendsListAdaptor extends RecyclerView.Adapter<FriendsListAdaptor.ViewHolder>{

    private static final String TAG = "FriendsListAdaptor";
    private List<User> friends;

    //interface for when a drinkItem is clicked
    public interface IFriendItemClickedListener {
        void onFriendClicked(String id);
    }

    private IFriendItemClickedListener friendListener;

    public FriendsListAdaptor(List<User> friendsList, IFriendItemClickedListener listener) {
        friends = friendsList;
        this.friendListener = listener;
    }

    public void setData(List<User> friends) { this.friends = this.friends; }

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
                friendListener.onFriendClicked(friendClicked.id);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    @NonNull
    @Override
    public FriendsListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
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
        holder.txtFriendId.setText(friends.get(position).id);
        holder.txtFriendName.setText(friends.get(position).name);

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
}
