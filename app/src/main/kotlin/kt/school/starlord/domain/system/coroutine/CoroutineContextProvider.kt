package kt.school.starlord.domain.system.coroutine

import kotlin.coroutines.CoroutineContext

/**
 * Interface for describing coroutine contexts.
 */
interface CoroutineContextProvider {
    val main: CoroutineContext
    val computation: CoroutineContext
    val io: CoroutineContext
}
