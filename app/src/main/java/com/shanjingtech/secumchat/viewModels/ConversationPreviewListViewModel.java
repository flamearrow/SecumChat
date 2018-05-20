package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.db.ConversationPreview;

import java.util.List;

public class ConversationPreviewListViewModel extends SecumDBViewModel {

    public ConversationPreviewListViewModel(@NonNull Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
    }

    public LiveData<List<ConversationPreview>> getConversationPreviews(String userId) {
        return messageDAO.liveConversationPreviewOwnedBy(userId);
    }
}
