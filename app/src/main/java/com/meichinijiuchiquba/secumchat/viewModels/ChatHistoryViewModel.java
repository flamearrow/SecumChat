package com.meichinijiuchiquba.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.meichinijiuchiquba.secumchat.db.Message;

import java.util.List;

/**
 * Created by flamearrow on 3/6/18.
 */

public class ChatHistoryViewModel extends SecumDBViewModel {

    public ChatHistoryViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Message>> getLiveHistoryWithGroupId(String groupId) {
        return messageDAO.liveHistoryWithGroupId(groupId);
    }
}
