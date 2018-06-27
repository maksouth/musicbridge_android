package com.yeket.music.bridge.services.player;

import com.yeket.music.bridge.ui.views.BaseContract;

public interface PlayerContract {

    interface View extends BaseContract.View{
        void changePlayerButton(boolean isPlaying);
        void setTrackName(String trackName);
        void setArtistName(String artistName);
    }

}
