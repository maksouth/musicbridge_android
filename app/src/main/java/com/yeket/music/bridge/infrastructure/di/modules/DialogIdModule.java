package com.yeket.music.bridge.infrastructure.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DialogIdModule {

    private String dialogId;

    public DialogIdModule(String dialogId){
        this.dialogId = dialogId;
    }

    @Provides
    @Named("dialogId")
    String provideDialofId(){
        return dialogId;
    }

}
