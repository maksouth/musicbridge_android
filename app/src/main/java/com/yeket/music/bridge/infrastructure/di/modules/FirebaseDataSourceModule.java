package com.yeket.music.bridge.infrastructure.di.modules;

import com.yeket.music.bridge.datasource.chat.DialogDataSource;
import com.yeket.music.bridge.datasource.chat.FirebaseDialogDataSource;
import com.yeket.music.bridge.datasource.chat.FirebaseMessagesDataSource;
import com.yeket.music.bridge.datasource.chat.MessagesDataSource;
import com.yeket.music.bridge.datasource.likes.FirebaseLikesDataSource;
import com.yeket.music.bridge.datasource.likes.LikesDataSource;
import com.yeket.music.bridge.datasource.music.FirebaseMusicDataSource;
import com.yeket.music.bridge.datasource.music.MusicDataSource;
import com.yeket.music.bridge.datasource.settings.FirebaseSettingsDataSource;
import com.yeket.music.bridge.datasource.settings.SettingsDataSource;
import com.yeket.music.bridge.datasource.tokens.FirebaseTokensDataSource;
import com.yeket.music.bridge.datasource.tokens.TokensDataSource;
import com.yeket.music.bridge.datasource.users.FirebaseRecommendationsDataSource;
import com.yeket.music.bridge.datasource.users.FirebaseUserPreferencesSource;
import com.yeket.music.bridge.datasource.users.FirebaseUsersDataSource;
import com.yeket.music.bridge.datasource.users.RecommendationsDataSource;
import com.yeket.music.bridge.datasource.users.UserPreferencesSource;
import com.yeket.music.bridge.datasource.users.UsersDataSource;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FirebaseDataSourceModule {

    @Binds
    abstract DialogDataSource provideDialogDataSource(FirebaseDialogDataSource dialogDataSource);

    @Binds
    abstract MessagesDataSource provideMessagesDataSource(FirebaseMessagesDataSource messagesDataSource);

    @Binds
    abstract LikesDataSource provideLikesDataSource(FirebaseLikesDataSource likesDataSource);

    @Binds
    abstract MusicDataSource provideMusicDataSource(FirebaseMusicDataSource musicDataSource);

    @Binds
    abstract SettingsDataSource provideSettingsDataSource(FirebaseSettingsDataSource settingsDataSource);

    @Binds
    abstract TokensDataSource provideTokensDataSource(FirebaseTokensDataSource dataSource);

    @Binds
    abstract RecommendationsDataSource provideRecommendationsDataSource(FirebaseRecommendationsDataSource dataSource);

    @Binds
    abstract UserPreferencesSource provideUserPreferencesSource(FirebaseUserPreferencesSource userPreferencesSource);

    @Binds
    abstract UsersDataSource provideUsersDataSource(FirebaseUsersDataSource usersDataSource);
}
