package com.example.kr_linechatappication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kr_linechatappication.Data.ChatData;
import com.example.kr_linechatappication.Data.CircleTransform;
import com.example.kr_linechatappication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<ChatData> chatDataArrayList;
    public static final int VIEW_TYPE_SELF_CHAT = 1;
    public static final int VIEW_TYPE_FRIEND_CHAT = 2;

    public ChatAdapter(Context context) {
        this.mContext = context;
        this.chatDataArrayList = new ArrayList<ChatData>();
    }

    public void addItem(ChatData chatData) {
        chatDataArrayList.add(chatData);
        notifyDataSetChanged();
        Log.v("test123","111");
        Log.v("test123",""+chatDataArrayList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("test123","2");
        View view;
        if(viewType == VIEW_TYPE_SELF_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_self_chat, parent, false);
            Log.v("test123","3");
            return new SelfChatViewHolder(view);
        } else if(viewType == VIEW_TYPE_FRIEND_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_friend_chat, parent, false);
            Log.v("test123","4");
            return new FriendChatViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.v("test123","5");
        if(getItemViewType(position) == VIEW_TYPE_SELF_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((SelfChatViewHolder) holder).bindToSelf(chatData);
        } else if(getItemViewType(position) == VIEW_TYPE_FRIEND_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((FriendChatViewHolder) holder).bindToSelf(chatData);
        }

    }

    @Override
    public int getItemCount() {
        Log.v("test123","6");
        return chatDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.v("test123","7");
        return chatDataArrayList.get(position).getType();
    }

    class SelfChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;

        public SelfChatViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bindToSelf(ChatData chatData) {
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtContent.setText(chatData.getContent());

        }
    }

    class FriendChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHeader;
        TextView txtContent;
        public FriendChatViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.v("test123","8");
            imgHeader = (ImageView) itemView.findViewById(R.id.img_header);
            Picasso.get().load(R.drawable.ic_launcher).transform(new CircleTransform()).into(imgHeader);
        }

        public void bindToSelf(ChatData chatData) {
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtContent.setText(chatData.getContent());
        }
    }
}
