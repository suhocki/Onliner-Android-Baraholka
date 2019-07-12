package kt.school.starlord.model.coroutine

import kotlinx.coroutines.Dispatchers
import kt.school.starlord.domain.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

/**
 * Provides coroutines context for application.
 */
class AppCoroutineContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Main
    override val io: CoroutineContext = Dispatchers.IO
    override val default: CoroutineContext = Dispatchers.Default
}
