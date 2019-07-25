package kt.school.starlord

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider

class TestContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val io: CoroutineContext = Dispatchers.Unconfined
    override val default: CoroutineContext = Dispatchers.Unconfined
}
