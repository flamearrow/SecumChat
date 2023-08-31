package com.meichinijiuchiquba.secumchat.message;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.meichinijiuchiquba.secumchat.R;
import com.meichinijiuchiquba.secumchat.db.Message;
import com.meichinijiuchiquba.secumchat.db.TimestampConverter;
import com.meichinijiuchiquba.secumchat.ui.CircleImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adapter for chatting, currently only supports text
 * TODO: support image
 */
public class SecumMessageAdapter extends RecyclerView.Adapter<SecumMessageAdapter
        .TextMessageViewHolder> {
    private static final int TYPE_SELF_TEXT_MESSAGE = 0;
    private static final int TYPE_OTHER_MESSAGE = 1;

    private final String ownerName;

    private List<Message> messageList;

    private boolean shouldShowImage;

    public SecumMessageAdapter(String ownerName, boolean shouldShowImage) {
        this.ownerName = ownerName;
        this.shouldShowImage = shouldShowImage;
        messageList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        int ret = isSelf(messageList.get(position)) ? TYPE_SELF_TEXT_MESSAGE :
                TYPE_OTHER_MESSAGE;

        Message m = messageList.get(position);
        return ret;
    }

    private boolean isSelf(Message message) {
        return message.getFrom().equals(ownerName);
    }

    @Override
    public TextMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_OTHER_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_peer,
                    parent, false), shouldShowImage);
        } else if (viewType == TYPE_SELF_TEXT_MESSAGE) {
            return new TextMessageViewHolder(inflater.inflate(R.layout.text_message_self,
                    parent, false), false);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(TextMessageViewHolder textMessageViewHolder, int position) {
        textMessageViewHolder.bindMessage(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void replaceItems(List<Message> newMessages) {
        this.messageList = newMessages;
        notifyDataSetChanged();
    }

    public static final class TextMessageViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime;
        ImageView imgContent;
        TextView txtContent;
        CircleImageView profilePicImage;
        // TODO: display/hide this
        ProgressBar progressBar;

        boolean shouldShowImage;

        public TextMessageViewHolder(View itemView, boolean shouldShowImage) {
            super(itemView);
            this.shouldShowImage = shouldShowImage;
            txtTime = itemView.findViewById(R.id.txt_time);
            txtContent = itemView.findViewById(R.id.txt_content);
            profilePicImage = itemView.findViewById(R.id.img_user_image);
            progressBar = itemView.findViewById(R.id.sent_indicator);
            imgContent = itemView.findViewById(R.id.img_content);

        }

        public void bindMessage(Message message) {
            txtTime.setText(TimestampConverter.fromLongHourMinuteOnly(message
                    .getTime()));
            txtContent.setText("");
            if (shouldShowImage && !message.getImageUrl().isEmpty()) {
                txtContent.setVisibility(View.GONE);
                imgContent.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getImageUrl()).into(imgContent);
                imgContent.setOnLongClickListener(view -> {
                    saveImage(view.getContext(), (BitmapDrawable) imgContent.getDrawable());
                    return true;
                });
            } else {
                imgContent.setVisibility(View.GONE);
                txtContent.setVisibility(View.VISIBLE);
                txtContent.setText(message.getContent());
                itemView.setOnLongClickListener(view -> {
                    Context context = view.getContext();
                    ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                            .setPrimaryClip(ClipData.newPlainText("AI reply", txtContent.getText()));
                    Toast.makeText(context, context.getResources().getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                    return true;
                });
            }
        }

        private void saveImage(Context context, BitmapDrawable drawable) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AIMars");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, UUID.randomUUID().toString() + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream out = new FileOutputStream(file)) {
                drawable.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Uri imageUriFP = FileProvider.getUriForFile(context, context.getPackageName() + ".com.vansuita.pickimage.provider", file);
            // Media scanner to update the gallery
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUriFP));

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUriFP);
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        }
    }
}
