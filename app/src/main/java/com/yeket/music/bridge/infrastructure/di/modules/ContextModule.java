package com.yeket.music.bridge.infrastructure.di.modules;

import android.app.Activity;
import android.content.Context;

import com.yeket.music.bridge.infrastructure.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maksouth on 30.12.17.
 */
@Module
public class ContextModule {

    private Activity activity;
    private Context context;

    public ContextModule(Activity activity){
        this.activity = activity;
    }

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @PerActivity
    Context provideContext(){

        return activity == null
                ? context
                : activity.getApplicationContext();
    }

    // TODO: 08.01.18 per activity annotation
    @Provides
    @PerActivity
    Activity provideActivity(){
        return activity;
    }

}
