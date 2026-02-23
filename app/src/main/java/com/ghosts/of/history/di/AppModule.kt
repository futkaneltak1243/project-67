package com.ghosts.of.history.di

import com.ghosts.of.history.data.services.VoiceCommandProcessorImpl
import com.ghosts.of.history.domain.services.VoiceCommandProcessor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import com.ghosts.of.history.data.services.ConfirmationManagerImpl
import com.ghosts.of.history.domain.services.ConfirmationManager
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.ghosts.of.history.domain.services.TTSManager
import com.ghosts.of.history.data.services.TTSManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindVoiceCommandProcessor(impl: VoiceCommandProcessorImpl): VoiceCommandProcessor

    @Binds
    @Singleton
    abstract fun bindConfirmationManager(impl: ConfirmationManagerImpl): ConfirmationManager

    @Binds
    @Singleton
    abstract fun bindTTSManager(ttsManagerImpl: TTSManagerImpl): TTSManager
}
