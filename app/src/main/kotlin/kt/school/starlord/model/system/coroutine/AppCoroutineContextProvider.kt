package kt.school.starlord.model.system.coroutine

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider

/**
 * Provides coroutines context for application.
 */
class AppCoroutineContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
    override val default: CoroutineContext = Dispatchers.Default
}
