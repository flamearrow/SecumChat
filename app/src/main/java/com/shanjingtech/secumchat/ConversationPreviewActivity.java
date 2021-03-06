package com.shanjingtech.secumchat;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanjingtech.secumchat.db.ConversationPreview;
import com.shanjingtech.secumchat.db.TimestampConverter;
import com.shanjingtech.secumchat.message.SecumMessageActivity;
import com.shanjingtech.secumchat.viewModels.ConversationPreviewListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Display ongoing conversation previews.
 */

public class ConversationPreviewActivity extends SecumTabbedActivity {
    private static final String TAG = "ConversationPreview";
    private RecyclerView recyclerView;
    private ConversationPreviewListViewModel conversationPreviewListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeRecyclerView();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fullscreen_recyclerview_container;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_conversation;
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conversationPreviewListViewModel = ViewModelProviders.of(this).get
                (ConversationPreviewListViewModel.class);

        ConversationAdapter conversationAdapter = new ConversationAdapter();
        conversationPreviewListViewModel.getConversationPreviews(currentUserProvider.getUser()
                .getUsername())
                .observe(
                        this,
                        items -> conversationAdapter.replaceItems(items));

        recyclerView.setAdapter(conversationAdapter);


    }

    /**
     * Represent a row of a conversation preview, add a number bubble if there're unread messages
     */
    class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter
            .ConversationPreviewHolder> {
        List<ConversationPreview> conversationPreviews;

        ConversationAdapter() {
            this.conversationPreviews = new ArrayList<>();
        }

        @Override
        public ConversationPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ConversationPreviewHolder(LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.conversation_preview_item_constraint, parent, false));
        }

        @Override
        public void onBindViewHolder(ConversationPreviewHolder holder, int position) {
            ConversationPreview conversationPreview = conversationPreviews.get(position);
            holder.userName.setText(conversationPreview.getPeerNickName());
            holder.lastMessage.setText(conversationPreview.getLastUnreadContent());
            int unreadCount = conversationPreview.getUnreadCount();
            if (unreadCount > 0) {
                holder.unreadCount.setText("" + unreadCount);
                holder.unreadCount.setVisibility(View.VISIBLE);
            } else {
                holder.unreadCount.setVisibility(View.GONE);
            }
            holder.sentTime.setText(TimestampConverter.fromLongHourMinuteOnly(
                    conversationPreview.getTime()));

            holder.groupId = conversationPreview.getGroupId();
            holder.peerNickName = conversationPreview.getPeerNickName();
            holder.peerUserName = conversationPreview.getPeerUserName();
        }

        @Override
        public int getItemCount() {
            return conversationPreviews.size();
        }

        public void replaceItems(List<ConversationPreview> newConversationPreviews) {
            this.conversationPreviews = newConversationPreviews;
            notifyDataSetChanged();
        }

        final class ConversationPreviewHolder extends RecyclerView.ViewHolder {
            ImageView avatar;
            TextView userName;
            TextView lastMessage;
            TextView sentTime;
            TextView unreadCount;
            String groupId;
            String peerNickName;
            String peerUserName;

            public ConversationPreviewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(view -> {
                    // access groupId
                    Log.d(TAG, groupId);
                    Intent intent = new Intent(
                            ConversationPreviewActivity.this,
                            SecumMessageActivity.class);
                    intent.putExtra(SecumMessageActivity.PEER_USER_NAME, peerUserName);
                    intent.putExtra(SecumMessageActivity.GROUP_ID, groupId);
                    startActivity(intent);
                });
                avatar = itemView.findViewById(R.id.avatar);
                userName = itemView.findViewById(R.id.user_name);
                lastMessage = itemView.findViewById(R.id.last_message);
                sentTime = itemView.findViewById(R.id.sent_time);
                unreadCount = itemView.findViewById(R.id.unread_count);
            }
        }

    }
}
