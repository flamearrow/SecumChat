package com.meichinijiuchiquba.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.meichinijiuchiquba.secumchat.SecumApplication;
import com.meichinijiuchiquba.secumchat.db.BotConversationPreview;
import com.meichinijiuchiquba.secumchat.db.ConversationPreview;

import java.util.List;

public class ConversationPreviewListViewModel extends SecumDBViewModel {

    public ConversationPreviewListViewModel(@NonNull Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
    }

    public LiveData<List<ConversationPreview>> getConversationPreviews(String userId) {
        return messageDAO.liveConversationPreviewOwnedBy(userId);
    }

    public LiveData<List<BotConversationPreview>> getBotPreviews(String userId) {
        return messageDAO.botChatsPreview(userId);
    }
}
