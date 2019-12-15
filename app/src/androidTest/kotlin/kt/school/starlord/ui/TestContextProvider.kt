package kt.school.starlord.ui

import kotlinx.coroutines.Dispatchers
import kt.school.starlord.domain.system.coroutine.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

class TestContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val io: CoroutineContext = Dispatchers.Unconfined
    override val computation: CoroutineContext = Dispatchers.Unconfined
}
