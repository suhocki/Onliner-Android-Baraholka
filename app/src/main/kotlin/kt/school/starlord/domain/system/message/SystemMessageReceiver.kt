package kt.school.starlord.domain.system.message

/**
 * Interface with basic methods to tell user what is happening now.
 */
interface SystemMessageReceiver {
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
