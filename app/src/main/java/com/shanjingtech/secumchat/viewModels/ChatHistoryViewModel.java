package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.db.Message;
import com.shanjingtech.secumchat.db.MessageDAO;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by flamearrow on 3/6/18.
 */

public class ChatHistoryViewModel extends AndroidViewModel {
    @Inject
    MessageDAO messageDAO;

    public ChatHistoryViewModel(@NonNull Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
    }

    public LiveData<List<Message>> getLiveHistoryWithGroupId(String groupId) {
        return messageDAO.liveHistoryWithGroupId(groupId);
    }
}
