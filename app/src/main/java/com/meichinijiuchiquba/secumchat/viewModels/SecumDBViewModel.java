package com.meichinijiuchiquba.secumchat.viewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import com.meichinijiuchiquba.secumchat.SecumApplication;
import com.meichinijiuchiquba.secumchat.db.MessageDAO;
import com.meichinijiuchiquba.secumchat.db.UserDAO;

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
