package kt.school.starlord.domain.system.view

/**
 * Controls displaying snackbar with error text.
 */
interface ErrorSnackbar {

    /**
     * Display`s snackbar with error text until user dismissed it.
     */
    fun show(error: Throwable)

    /**
     * Hides snackbar from window.
     */
    fun dismiss()
}
