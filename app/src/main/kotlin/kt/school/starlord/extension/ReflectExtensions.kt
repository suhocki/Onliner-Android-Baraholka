package kt.school.starlord.extension

import java.lang.reflect.Field

/**
 * Encapsulates calls to reflection API for unit-tests.
 */
fun Any.getClassDeclaredFields(): Array<Field> = javaClass.declaredFields
