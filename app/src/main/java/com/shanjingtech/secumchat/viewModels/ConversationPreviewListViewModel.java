package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.db.ConversationPreview;
import com.shanjingtech.secumchat.db.MessageDAO;

import java.util.List;

import javax.inject.Inject;

public class ConversationPreviewListViewModel extends AndroidViewModel {


    @Inject
    MessageDAO messageDAO;
    // TODO: use CurrentUserProvider

    public ConversationPreviewListViewModel(@NonNull Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
    }

    public LiveData<List<ConversationPreview>> getConversationPreviews(String userId) {
        return messageDAO.liveConversationPreviewOwnedBy(userId);
    }
}
