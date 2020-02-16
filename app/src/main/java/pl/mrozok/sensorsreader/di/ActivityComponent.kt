package pl.mrozok.sensorsreader.di

import dagger.Subcomponent
import pl.mrozok.sensorsreader.presentation.ReadingsActivity

@Subcomponent(modules = [PresentationModule::class, SensorsModule::class])
interface ActivityComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ActivityComponent
        fun sensorsModule(module: SensorsModule): Builder

    }

    fun inject(activity: ReadingsActivity)

}
