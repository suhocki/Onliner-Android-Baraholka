package kt.school.starlord.ui.global.extension

import java.lang.reflect.Field

/**
 * Encapsulates calls to reflection API for unit-tests.
 */
fun Any.getClassDeclaredFields(): Array<Field> = javaClass.declaredFields
