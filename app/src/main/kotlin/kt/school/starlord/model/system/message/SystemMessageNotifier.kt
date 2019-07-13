package kt.school.starlord.model.system.message

import kt.school.starlord.domain.system.message.SystemMessageReceiver

/**
 * Class that has a link on message receiver for sending messages.
 */
class SystemMessageNotifier : SystemMessageReceiver {
    var systemMessageReceiver: SystemMessageReceiver? = null

    override fun showMessage(message: String) {
        systemMessageReceiver?.showMessage(message)
    }

    override fun showError(error: Throwable) {
        systemMessageReceiver?.showError(error)
    }

    override fun showProgress(visible: Boolean) {
        systemMessageReceiver?.showProgress(visible)
    }
}
