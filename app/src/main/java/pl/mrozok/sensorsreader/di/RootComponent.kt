package pl.mrozok.sensorsreader.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [InfrastructureModule::class])
interface RootComponent {

    fun activitySubcomponent(): ActivityComponent.Builder

}
