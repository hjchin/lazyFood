package world.trav.lazyfood.shared
import timber.log.Timber

actual object Log {
    actual fun d(message: String) {
        Timber.d(message)
    }
}