package com.shanjingtech.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import com.shanjingtech.secumchat.SecumApplication;
import com.shanjingtech.secumchat.db.MessageDAO;
import com.shanjingtech.secumchat.db.UserDAO;

import javax.inject.Inject;

public abstract class SecumDBViewModel extends AndroidViewModel {
    @Inject
    UserDAO userDAO;

    @Inject
    MessageDAO messageDAO;

    public SecumDBViewModel(Application application) {
        super(application);
        ((SecumApplication) application).getNetComponet().inject(this);
    }

}
