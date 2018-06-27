package com.yeket.music.bridge.ui.views.dialogs;

import com.yeket.music.bridge.models.chat.Dialog;
import com.yeket.music.bridge.ui.views.BaseContract;

import java.util.List;

public interface DialogListContract {

    interface View extends BaseContract.View{
        void addDialog(Dialog dialog);
        void addDialogs(List<Dialog> dialogs);
        void updateLastMessage(Dialog dialog);
        void goToDialogScreen(String dialogId, String senderId);

        void showOverlay(boolean showMessage);
        void hideOverlay();
    }

    interface Presenter{
        void start();
        void dialogClick(Dialog dailog);
        void stop();
    }

}
