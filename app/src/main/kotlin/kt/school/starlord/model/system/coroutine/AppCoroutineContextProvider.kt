package kt.school.starlord.model.system.coroutine

import kotlinx.coroutines.Dispatchers
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

/**
 * Provides coroutines context for application.
 */
class AppCoroutineContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
    override val computation: CoroutineContext = Dispatchers.Default
}
