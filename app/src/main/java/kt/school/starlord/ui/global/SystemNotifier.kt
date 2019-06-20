package kt.school.starlord.ui.global

/**
 * Interface with basic methods to tell user what is happening now.
 */
interface SystemNotifier {
    /**
     * Display a toast with message.
     */
    fun showMessage(message: String)

    /**
     * Display a toast with error.
     */
    fun showError(error: Throwable)

    /**
     * Apply progress bar visibility.
     */
    fun showProgress(visible: Boolean)
}
