package kt.school.starlord.model.system.message

import kt.school.starlord.domain.system.message.SystemMessageReceiver

/**
 * Class that has a link on message receiver for sending messages.
 * Usually it is used to link activity with fragments without casting to AppActivity class.
 * Fixes lint issue.
 */
class SystemMessageNotifier : SystemMessageReceiver {
    var systemMessageReceiver: SystemMessageReceiver? = null

    override fun showMessage(message: String) {
        requireSystemMessageReceiver().showMessage(message)
    }

    override fun showError(error: Throwable) {
        requireSystemMessageReceiver().showError(error)
    }

    override fun showProgress(visible: Boolean) {
        requireSystemMessageReceiver().showProgress(visible)
    }

    private fun requireSystemMessageReceiver() =
        systemMessageReceiver ?: error("SystemMessageReceiver $this not attached to a context.")
}
