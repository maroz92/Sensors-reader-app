package pl.mrozok.sensorsreader.di

import android.content.Context

object GraphBuilder {

    fun build(context: Context): RootComponent =
        DaggerRootComponent.builder()
            .infrastructureModule(InfrastructureModule(context))
            .build()

}
