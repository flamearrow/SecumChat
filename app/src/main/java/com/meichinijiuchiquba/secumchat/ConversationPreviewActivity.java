package com.meichinijiuchiquba.secumchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meichinijiuchiquba.secumchat.db.BotConversationPreview;
import com.meichinijiuchiquba.secumchat.db.TimestampConverter;
import com.meichinijiuchiquba.secumchat.message.SecumMessageActivity;
import com.meichinijiuchiquba.secumchat.util.BotUtils;
import com.meichinijiuchiquba.secumchat.viewModels.ConversationPreviewListViewModel;

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
        observeForMessagePreviews();
    }

    @Override
    protected int getContentResId() {
        return R.layout.fullscreen_recyclerview_container;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.menu_conversation;
    }

    private ConversationAdapter conversationAdapter;

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conversationPreviewListViewModel = new ViewModelProvider(this).get
                (ConversationPreviewListViewModel.class);

        conversationAdapter = new ConversationAdapter();
        recyclerView.setAdapter(conversationAdapter);
    }

    private void observeForMessagePreviews() {
        conversationPreviewListViewModel.getBotPreviews(
                currentUserProvider.getUser().userId).observe(this, conversationPreviews -> {
            for (BotConversationPreview c : conversationPreviews) {
                Log.d("BGLM", "new preview " + c);
                conversationAdapter.replaceItems(conversationPreviews);
            }
        });
    }

    /**
     * Represent a row of a conversation preview, add a number bubble if there're unread messages
     */
    class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter
            .ConversationPreviewHolder> {
        List<BotConversationPreview> conversationPreviews;

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
            BotConversationPreview conversationPreview = conversationPreviews.get(position);
            Log.d("BGLM", "from_username: " + conversationPreview.from_username);
            Log.d("BGLM", "bot name: " + BotUtils.nickNameToBotName(conversationPreview.from_username));
            holder.userName.setText(BotUtils.idToBotName(conversationPreview.from_username));
            holder.lastMessage.setText(conversationPreview.last_message);
//            int unreadCount = conversationPreview.getUnreadCount();
//            if (unreadCount > 0) {
//                holder.unreadCount.setText("" + unreadCount);
//                holder.unreadCount.setVisibility(View.VISIBLE);
//            } else {
//                holder.unreadCount.setVisibility(View.GONE);
//            }
            holder.sentTime.setText(TimestampConverter.fromLongHourMinuteOnly(
                    conversationPreview.time));

            holder.groupId = conversationPreview.group_id;
            holder.peerNickName = BotUtils.idToBotName(conversationPreview.from_username);
            holder.peerUserName = conversationPreview.from_username;
        }

        @Override
        public int getItemCount() {
            return conversationPreviews.size();
        }

        public void replaceItems(List<BotConversationPreview> newConversationPreviews) {
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
