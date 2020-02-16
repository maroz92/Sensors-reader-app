package pl.mrozok.sensorsreader.infrastructure

import android.view.View
import android.view.ViewGroup
import pl.mrozok.core.domain.BaseSensor
import pl.mrozok.core.infrastructure.Reading
import pl.mrozok.core.infrastructure.ScreenTouchReading
import java.util.*
import kotlin.collections.HashSet

class ScreenTouchSensor(private val view: ViewGroup) : BaseSensor() {

    private val allViews = HashSet<View>()

    override fun startMonitoring(readingConsumer: (Reading) -> Unit) {
        super.startMonitoring(readingConsumer)
        getAllChildren(view)

        allViews.forEach { view ->
            view.setOnTouchListener { _, event ->
                readingConsumer.invoke(
                    ScreenTouchReading(
                        Date(event.eventTime),
                        event.x,
                        event.y
                    )
                )
                false
            }
        }
    }

    private fun getAllChildren(view: View) {
        if (view.isClickable) allViews.add(view)
        if ((view is ViewGroup).not()) return
        val viewGroup = view as ViewGroup
        allViews.add(view)
        (0 until viewGroup.childCount)
            .forEach { index ->
                getAllChildren(viewGroup.getChildAt(index))
            }
    }

    override fun stopMonitoring() {
        view.setOnTouchListener(null)
    }

}
