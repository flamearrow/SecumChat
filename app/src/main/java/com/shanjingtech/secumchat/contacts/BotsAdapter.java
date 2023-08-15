package com.shanjingtech.secumchat.contacts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.message.SecumMessageActivity;
import com.shanjingtech.secumchat.model.ContactRequest;
import com.shanjingtech.secumchat.model.CreateGroupRequest;
import com.shanjingtech.secumchat.model.MessageGroup;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.BotUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BotsAdapter extends RecyclerView.Adapter {
    private List<ContactRequest> contactRequests;

    private RecyclerView recyclerView;
    private SecumAPI secumAPI;
    private ContactsActivity activity;

    public BotsAdapter(ContactsActivity activity, RecyclerView recyclerView, SecumAPI secumAPI) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.secumAPI = secumAPI;
        contactRequests = new ArrayList<>();
    }

    public void updateContacts(List<ContactRequest> contacts) {
        this.contactRequests = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(
                R.layout.contact_item, parent, false);
        // for other 3 types, make the item clickable to see the users profile
        view.setOnClickListener(v -> {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            Context context = parent.getContext();
            String profileUserId = contactRequests.get(itemPosition).user.userId;
            secumAPI.createGroup(new CreateGroupRequest(Integer.parseInt(profileUserId))).enqueue(new Callback<MessageGroup>() {
                @Override
                public void onResponse(Call<MessageGroup> call, Response<MessageGroup> response) {
                    Intent intent = new Intent(
                            activity,
                            SecumMessageActivity.class);
                    intent.putExtra(SecumMessageActivity.PEER_USER_NAME, profileUserId);
                    intent.putExtra(SecumMessageActivity.GROUP_ID, response.body().msgGrpId);
                    context.startActivity(intent);
                }

                @Override
                public void onFailure(Call<MessageGroup> call, Throwable t) {
                    Log.d("BGLM", "create group failure");
                }
            });
        });
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // offset label and pending pointer
        User contact = contactRequests.get(position).user;
        ((ContactViewHolder) holder).name.setText(BotUtils.nickNameToBotName(contact.getNickname()));
    }


    @Override
    public int getItemCount() {
        return contactRequests.size();
    }
}
