package com.shanjingtech.secumchat.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.TimestampConverter;
import com.shanjingtech.secumchat.ui.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for chatting, currently only supports text
 * TODO: support image
 */
public class SecumMessageAdapter extends RecyclerView.Adapter<SecumMessageAdapter
        .TextMessageViewHolder> {
    private static final int TYPE_SELF_TEXT_MESSAGE = 0;
    private static final int TYPE_OTHER_TEXT_MESSAGE = 1;

    private final String ownerName;

    private List<Message> messageList;


    public SecumMessageAdapter(String ownerName) {
        this.ownerName = ownerName;
        messageList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        int ret = isSelf(messageList.get(position)) ? TYPE_SELF_TEXT_MESSAGE :
                TYPE_OTHER_TEXT_MESSAGE;

        Message m = messageList.get(position);
        return ret;
    }

    private boolean isSelf(Message message) {
        return message.getFrom().equals(ownerName);
    }

    @Override
    public TextMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_OTHER_TEXT_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_peer,
                    parent, false));
        } else if (viewType == TYPE_SELF_TEXT_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_self,
                    parent, false));
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(TextMessageViewHolder textMessageViewHolder, int position) {
        Message message = messageList.get(position);
        textMessageViewHolder.txtTime.setText(TimestampConverter.fromLongHourMinuteOnly(message
                .getTime()));
        textMessageViewHolder.txtContent.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void replaceItems(List<Message> newMessages) {
        this.messageList = newMessages;
        notifyDataSetChanged();
    }

    public final class TextMessageViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime;
        TextView txtContent;
        CircleImageView profilePicImage;
        // TODO: display/hide this
        ProgressBar progressBar;

        public TextMessageViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtContent = itemView.findViewById(R.id.txt_content);
            profilePicImage = itemView.findViewById(R.id.img_user_image);
            progressBar = itemView.findViewById(R.id.sent_indicator);
        }

    }
}
