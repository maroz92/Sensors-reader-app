package pl.mrozok.core.infrastructure

interface ReadingsPublisher {
    fun publish(readings: List<Reading>)
}
