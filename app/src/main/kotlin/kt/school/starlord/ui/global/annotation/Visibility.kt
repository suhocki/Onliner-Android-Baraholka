package kt.school.starlord.ui.global.annotation

import android.view.View
import androidx.annotation.IntDef

/**
 * Use to annotate field as Visibility identifier.
 */
@IntDef(View.VISIBLE, View.INVISIBLE, View.GONE)
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility
