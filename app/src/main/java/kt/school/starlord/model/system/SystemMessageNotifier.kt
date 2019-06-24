package kt.school.starlord.model.system

class SystemMessageNotifier: SystemMessageReceiver {
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
