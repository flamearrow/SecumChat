package com.shanjingtech.secumchat.message;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shanjingtech.secumchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for chatting, currently only supports text
 * TODO: support image
 */
public class SecumMessageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_SELF_TEXT_MESSAGE = 0;
    private static final int TYPE_OTHER_TEXT_MESSAGE = 1;
    List<Message> messageList = new ArrayList<>();


    public SecumMessageAdapter() {
        initiliazeFakeData();
    }

    void initiliazeFakeData() {
        messageList.add(new Message("mlgb", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb2", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb3", "Aug, 16, 2014", false));
        messageList.add(new Message("mlgb4", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb5", "Aug, 16, 2014", false));
        messageList.add(new Message("mlgb6", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb21", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb2", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb3", "Aug, 16, 2014", false));
        messageList.add(new Message("mlgb4", "Aug, 16, 2014", true));
        messageList.add(new Message("mlgb5", "Aug, 16, 2014", false));
        messageList.add(new Message("mlgb6", "Aug, 16, 2014", true));
    }

    public void addMessage(Message message) {
        messageList.add(message);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isSelf ? TYPE_SELF_TEXT_MESSAGE : TYPE_OTHER_TEXT_MESSAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_OTHER_TEXT_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_other, null));
        } else if (viewType == TYPE_SELF_TEXT_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_self, null));
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        TextMessageViewHolder textMessageViewHolder = (TextMessageViewHolder) holder;
        textMessageViewHolder.txtTime.setText(message.time);
        textMessageViewHolder.txtContent.setText(message.content);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

// TODO: rewrite this
class Message {
    public Message(String content, String time, boolean isSelf) {
        this.content = content;
        this.time = time;
        this.isSelf = isSelf;
    }

    String content;
    String time;
    boolean isSelf;
}