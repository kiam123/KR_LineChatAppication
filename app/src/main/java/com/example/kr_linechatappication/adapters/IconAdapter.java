package com.example.kr_linechatappication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.datas.ChatData;
import com.example.kr_linechatappication.datas.ChatData2;
import com.example.kr_linechatappication.datas.IconData;
import com.example.kr_linechatappication.datas.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import static androidx.recyclerview.widget.RecyclerView.*;

public class IconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<IconData> chatDataArrayList;
    Context mContext;
    public final int VIEW_TYPE_ICON = 1;
    ChatAdapter chatAdapter;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    public IconAdapter(Context context, ChatAdapter chatAdapter) {
        this.mContext = context;
        this.chatDataArrayList = new ArrayList<>();
        this.chatAdapter = chatAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_icon_view_pager, parent, false);
        return new IconChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ICON) {
            IconData iconData = (IconData) chatDataArrayList.get(position);
            ((IconChatViewHolder) holder).bindToSelf(iconData);
        }
    }

    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }

    public void addItem(IconData iconData) {
        chatDataArrayList.add(iconData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return chatDataArrayList.get(position).getView();
    }

    class IconChatViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView imageView;

        public IconChatViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bindToSelf(IconData iconData) {
//            Picasso.get().load(iconData.getmImageIcon()).into(imageView);
//            Log.v("asd123", iconData.getmImageIcon());
            Glide.with(mContext).load(iconData.getmImageIcon()).into(imageView);
        }

        @Override
        public void onClick(View v) {
            ChatData2 chatData = new ChatData2();
            chatData.setImageUrl(chatDataArrayList.get(getLayoutPosition()).getmImageIcon());
            chatData.setMessenge("");
            chatData.setReceiver(UserInfo.getInstance().getFriend());
            chatData.setSender(UserInfo.getInstance().getName());
            chatData.setType("image");

//            chatAdapter.addItem(chatData);


            String id = UserInfo.getInstance().getName();
            String friendId = UserInfo.getInstance().getFriend();
            String timesamp = Calendar.getInstance().getTimeInMillis()+"";

            myRef.child("chatInfo").child(timesamp).setValue(chatData);
            myRef.child("users").child(id).child("friends").child(friendId).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);
            myRef.child("users").child(friendId).child("friends").child(id).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);
//            chatAdapter.addItem(new ChatData("Kr Lee",chatDataArrayList.get(getLayoutPosition()).getmImageIcon(),"",11));
//            Toast.makeText(mContext, ""+getLayoutPosition(),Toast.LENGTH_LONG).show();
        }
    }
}
