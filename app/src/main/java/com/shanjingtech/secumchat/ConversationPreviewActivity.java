package com.shanjingtech.secumchat;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanjingtech.secumchat.db.ConversationPreview;
import com.shanjingtech.secumchat.db.TimestampConverter;
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

        // TODO: replace with CurrentUserProvider
        String userId = "phone_11";
        conversationPreviewListViewModel = ViewModelProviders.of(this).get
                (ConversationPreviewListViewModel.class);

        ConversationAdapter conversationAdapter = new ConversationAdapter();
        conversationPreviewListViewModel.getConversationPreviews(userId).observe(
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
                    (R.layout.conversation_preview_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ConversationPreviewHolder holder, int position) {
            ConversationPreview conversationPreview = conversationPreviews.get(position);
//            holder.avatar
            holder.userName.setText(conversationPreview.getFrom());
            holder.lastMessage.setText(conversationPreview.getLastUnreadContent());
            int unreadCount = conversationPreview.getUnreadCount();
            if (unreadCount > 0) {
                holder.unreadCount.setText("" + unreadCount);
                holder.unreadCount.setVisibility(View.VISIBLE);
            } else {
                holder.unreadCount.setVisibility(View.GONE);
            }
            holder.sentTime.setText(TimestampConverter.fromLongDateOnly(conversationPreview
                    .getTime()));

            holder.groupId = conversationPreview.getGroupId();
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

            public ConversationPreviewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(view -> {
                    // access groupId
                    Log.d(TAG, groupId);
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
