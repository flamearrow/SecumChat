package com.shanjingtech.secumchat.contacts;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.shanjingtech.secumchat.ProfileActivity;
import com.shanjingtech.secumchat.R;
import com.shanjingtech.secumchat.model.ApproveContactRequest;
import com.shanjingtech.secumchat.model.ContactRequest;
import com.shanjingtech.secumchat.model.GenericResponse;
import com.shanjingtech.secumchat.model.User;
import com.shanjingtech.secumchat.net.SecumAPI;
import com.shanjingtech.secumchat.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shanjingtech.secumchat.contacts.ContactsActivity.CONTACTS_TYPE_BLOCKED;
import static com.shanjingtech.secumchat.contacts.ContactsActivity.CONTACTS_TYPE_CONTACTS;
import static com.shanjingtech.secumchat.contacts.ContactsActivity.CONTACTS_TYPE_PENDING;
import static com.shanjingtech.secumchat.contacts.ContactsActivity.CONTACTS_TYPE_REQUESTED;

/**
 * Adapts contacts.
 */

public class ContactsAdapter extends RecyclerView.Adapter {
    private static final int PENDING_POINTER = 0;
    private static final int REQUESTED_POINTER = 3;
    private static final int BLOCKED_POINTER = 4;

    private static final int CONTACTS_LABEL = 1;
    private static final int CONTACTS = 2;

    private List<ContactRequest> contactRequests;

    private RecyclerView recyclerView;

    private Context context;

    private String adapterType;

    private SecumAPI secumAPI;

    public ContactsAdapter(RecyclerView recyclerView, String type, SecumAPI secumAPI) {
        this.recyclerView = recyclerView;
        this.secumAPI = secumAPI;
        context = recyclerView.getContext();
        adapterType = type;
    }

    public void updateContacts(List<ContactRequest> contacts) {
        this.contactRequests = contacts;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (adapterType.equals(CONTACTS_TYPE_BLOCKED) ||
                adapterType.equals(CONTACTS_TYPE_PENDING) ||
                adapterType.equals(CONTACTS_TYPE_REQUESTED)) {
            return position == 0 ? CONTACTS_LABEL : CONTACTS;
        }

        /**
         * For active contacts list:
         * 0: pending label
         * 1: contacts label
         * [2, 2+actibeSize-1]: contacts
         * 2+activeSize: requested label
         * 3+activeSize: blocked label
         */
        int contactsCount = contactRequests == null ? 0 : contactRequests.size();
        if (position == 0) {
            return PENDING_POINTER;
        } else if (position == 1) {
            return CONTACTS_LABEL;
        } else if (position <= contactsCount + 1) {
            return CONTACTS;
        } else if (position == 2 + contactsCount) {
            return REQUESTED_POINTER;
        } else if (position == 3 + contactsCount) {
            return BLOCKED_POINTER;
        } else return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == CONTACTS_LABEL) {
            return new TextLabelViewHolder(
                    inflater.inflate(
                            R.layout.text_label_view_item, parent, false));
        } else if (viewType == CONTACTS) {
            final View view = inflater.inflate(
                    R.layout.contact_item, parent, false);


            if (adapterType.equals(CONTACTS_TYPE_PENDING)) {
                // add a button to accept pending requests
                Button button = view.findViewById(R.id.action_button);
                button.setText(context.getText(R.string.approve));
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v -> {
                    int itemPosition = recyclerView.getChildLayoutPosition(view) - 1;
                    ContactRequest currentRequest = contactRequests.get(itemPosition);

                    secumAPI.approveContact(new ApproveContactRequest(Integer.parseInt(currentRequest.contactRequestId)))
                            .enqueue(new Callback<GenericResponse>() {
                                @Override
                                public void onResponse(Call<GenericResponse> call,
                                                       Response<GenericResponse>
                                                               response) {

                                    Log.d("BGLM", "add success: " + response);
                                    Toast.makeText(context, context.getResources()
                                                    .getString(R.string.add_success), Toast
                                                    .LENGTH_SHORT)
                                            .show();
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(Call<GenericResponse> call,
                                                      Throwable t) {
                                    Log.d("BGLM", "add failure: " + t);
                                    Toast.makeText(context, context.getResources()
                                            .getString(R.string.request_fail), Toast
                                            .LENGTH_SHORT).show();
                                }
                            });
                });
            } else {
                // for other 3 types, make the item clickable to see the users profile
                view.setOnClickListener(v -> {
                    int itemPosition = 0;
                    if (adapterType.equals(CONTACTS_TYPE_CONTACTS)) {
                        // offset pending and label
                        itemPosition = recyclerView.getChildLayoutPosition(view) - 2;
                    } else if (adapterType.equals(CONTACTS_TYPE_BLOCKED) || adapterType
                            .equals(CONTACTS_TYPE_REQUESTED)) {
                        // offset label
                        itemPosition = recyclerView.getChildLayoutPosition(view) - 1;
                    }

                    String currentUserName = contactRequests.get(itemPosition).user.getNickname();
                    Context context = parent.getContext();
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(Constants.PROFILE_USER_NAME, currentUserName);
                    context.startActivity(intent);
                });
            }
            return new ContactViewHolder(view);

        } else if (viewType == PENDING_POINTER || viewType == REQUESTED_POINTER || viewType ==
                BLOCKED_POINTER) {
            return new PointerLabelViewHolder(inflater.inflate(R.layout.pointer_label_view_item,
                    parent, false));
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CONTACTS: {
                if (adapterType == CONTACTS_TYPE_CONTACTS) {
                    // offset label and pending pointer
                    User contact = contactRequests.get(position - 2).user;
                    ((ContactViewHolder) holder).name.setText(contact.getNickname());
                } else if (contactRequests != null && contactRequests.size() > 0) {
                    // offset label
                    User contact = contactRequests.get((position - 1)).user;
                    ((ContactViewHolder) holder).name.setText(contact.getNickname());
                }
                break;

            }
            case CONTACTS_LABEL:
                String labelText;
                switch (adapterType) {
                    case CONTACTS_TYPE_BLOCKED:
                        labelText = context.getString(R.string.blocked);
                        break;
                    case CONTACTS_TYPE_PENDING:
                        labelText = context.getString(R.string.pending);
                        break;
                    case CONTACTS_TYPE_REQUESTED:
                        labelText = context.getString(R.string.requested);
                        break;
                    case CONTACTS_TYPE_CONTACTS:
                    default:
                        labelText = context.getString(R.string.contacts);
                        break;
                }
                ((TextLabelViewHolder) holder).label.setText(labelText);
                break;
            case REQUESTED_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .requested));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ContactsActivity.class);
                    intent.putExtra(ContactsActivity.CONTACTS_TYPE,
                            CONTACTS_TYPE_REQUESTED);
                    context.startActivity(intent);
                });
                break;
            case BLOCKED_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .blocked));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ContactsActivity.class);
                    intent.putExtra(ContactsActivity.CONTACTS_TYPE,
                            CONTACTS_TYPE_BLOCKED);
                    context.startActivity(intent);
                });
                break;
            case PENDING_POINTER:
                ((PointerLabelViewHolder) holder).textLabel.setText(context.getString(R.string
                        .pending));
                ((PointerLabelViewHolder) holder).itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ContactsActivity.class);
                    intent.putExtra(ContactsActivity.CONTACTS_TYPE,
                            CONTACTS_TYPE_PENDING);
                    context.startActivity(intent);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (adapterType.equals(CONTACTS_TYPE_CONTACTS)) {
            return 4 + (contactRequests == null ? 0 : contactRequests.size());
        } else {
            return 1 + (contactRequests == null ? 0 : contactRequests.size());
        }
    }
}
