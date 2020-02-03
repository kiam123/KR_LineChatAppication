package com.example.kr_linechatappication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kr_linechatappication.datas.ChatData;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.datas.UserInfo;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    //    ArrayList<ChatData> chatDataArrayList;
    ArrayList<ChatData> chatDataArrayList;
    RecyclerView chatRecyclerView;
    public static final int VIEW_TYPE_SELF_TEXT_CHAT = 10;
    public static final int VIEW_TYPE_SELF_IMAGE_CHAT = 11;
    public static final int VIEW_TYPE_FRIEND_TEXT_CHAT = 20;
    public static final int VIEW_TYPE_FRIEND_IMAGE_CHAT = 21;

    public ChatAdapter(Context context, RecyclerView chatRecyclerView) {
        this.mContext = context;
//        this.chatDataArrayList = new ArrayList<ChatData>();
        this.chatDataArrayList = new ArrayList<ChatData>();
        this.chatRecyclerView = chatRecyclerView;
    }

    public void addItem(ChatData chatData) {
        chatDataArrayList.add(chatData);
        notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(chatDataArrayList.size());
//        Log.v("test123", "111");
//        Log.v("test123", "" + chatDataArrayList.size());
    }

    public void clearItem() {
        chatDataArrayList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.v("test123", "2");
        View view;
        if (viewType == VIEW_TYPE_SELF_TEXT_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_self_text_chat, parent, false);
            return new SelfChatViewHolder(view);
        } else if (viewType == VIEW_TYPE_SELF_IMAGE_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_self_image_chat, parent, false);
            return new SelfImageChatViewHolder(view);
        } else if (viewType == VIEW_TYPE_FRIEND_TEXT_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_friend_text_chat, parent, false);
            return new FriendChatViewHolder(view);
        } else if (viewType == VIEW_TYPE_FRIEND_IMAGE_CHAT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_friend_image_chat, parent, false);
            return new FriendImageChatViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SELF_TEXT_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((SelfChatViewHolder) holder).bindToSelf(chatData);
        } else if (getItemViewType(position) == VIEW_TYPE_SELF_IMAGE_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((SelfImageChatViewHolder) holder).bindToSelf(chatData);
        } else if (getItemViewType(position) == VIEW_TYPE_FRIEND_TEXT_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((FriendChatViewHolder) holder).bindToSelf(chatData);
        } else if (getItemViewType(position) == VIEW_TYPE_FRIEND_IMAGE_CHAT) {
            ChatData chatData = (ChatData) chatDataArrayList.get(position);
            ((FriendImageChatViewHolder) holder).bindToSelf(chatData);
        }

    }

    @Override
    public int getItemCount() {
        return chatDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        Log.v("sender", UserInfo.getInstance().getName());
        if (chatDataArrayList.get(position).getSender().equals(UserInfo.getInstance().getName())) {
            if (chatDataArrayList.get(position).getType().equals("text")) {
//                Log.v("sender","1 "+chatDataArrayList.get(position).getSender()+" "+chatDataArrayList.get(position).getSender().equals(UserInfo.getInstance().getName()));
//                Log.v("sender","2 "+chatDataArrayList.get(position).getReceiver()+" "+chatDataArrayList.get(position).getReceiver().equals(UserInfo.getInstance().getName()));
                return VIEW_TYPE_SELF_TEXT_CHAT;
            }
            return VIEW_TYPE_SELF_IMAGE_CHAT;
        }
        else {
            if (chatDataArrayList.get(position).getType().equals("text")) {
                return VIEW_TYPE_FRIEND_TEXT_CHAT;
            }
            return VIEW_TYPE_FRIEND_IMAGE_CHAT;
        }
    }

    class SelfChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;

        public SelfChatViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bindToSelf(ChatData chatData) {
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtContent.setText(chatData.getMessenge());

        }
    }

    class SelfImageChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SelfImageChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

        public void bindToSelf(ChatData chatData) {
//            Picasso.get().load(chatData.getContent()).into(imageView);
            try {
//                Log.v("asd", chatData.getImageUrl());
                Glide.with(mContext).load(chatData.getImageUrl()).into(imageView);
            } catch (Exception e) {
                Log.v("error", e.toString());
            }
        }
    }

    class FriendChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHeader;
        TextView txtContent;

        public FriendChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHeader = (ImageView) itemView.findViewById(R.id.imgHeader);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
        }

        public void bindToSelf(ChatData chatData) {
//            Picasso.get().load(R.drawable.ic_launcher).transform(new CircleTransform()).into(imgHeader);
            try {
//                Log.v("errorasd123", UserInfo.getInstance().getFriendHeaderImage());
                Glide.with(mContext).load(UserInfo.getInstance().getFriendHeaderImage()).into(imgHeader);
//                Glide.with(mContext).load(UserInfo.getInstance().getFriendHeaderImage()).apply(RequestOptions.circleCropTransform()).into(imgHeader);
                txtContent.setText(chatData.getMessenge());
            } catch (Exception e) {
                Log.v("error", e.toString());
            }

            txtContent.setText(chatData.getMessenge());
        }
    }

    class FriendImageChatViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imgHeader;

        public FriendImageChatViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imgHeader = (ImageView) itemView.findViewById(R.id.imgHeader);
        }

        public void bindToSelf(ChatData chatData) {
//            Picasso.get().load(chatData.getContent()).into(imageView);
            try {
//                Log.v("errorasd123", "wtf "+chatData.getImageUrl());
//                Log.v("errorasd123", "wtf "+UserInfo.getInstance().getFriendHeaderImage());

//                Glide.with(mContext).load(UserInfo.getInstance().getFriendHeaderImage()).apply(RequestOptions.circleCropTransform()).into(imgHeader);
                Glide.with(mContext).load(UserInfo.getInstance().getFriendHeaderImage()).into(imgHeader);
                Glide.with(mContext).load(chatData.getImageUrl()).into(imageView);

            } catch (Exception e) {
                Log.v("error", e.toString());
            }
        }
    }
}
