package pl.mrozok.sensorsreader.di

import dagger.Module
import dagger.Provides
import pl.mrozok.core.domain.SensorsMonitorModule
import pl.mrozok.core.presentation.ReadingsPresenter
import pl.mrozok.core.presentation.ReadingsScreen

@Module
class PresentationModule {

    @Provides
    fun provideReadingsPresenter(sensorsMonitorModule: SensorsMonitorModule): ReadingsScreen.Presenter =
        ReadingsPresenter(sensorsMonitorModule)

}
