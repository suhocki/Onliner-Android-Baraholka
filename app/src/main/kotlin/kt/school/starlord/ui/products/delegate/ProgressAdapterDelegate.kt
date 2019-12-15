package kt.school.starlord.ui.products.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kt.school.starlord.R
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.global.extension.inflate
import kt.school.starlord.ui.products.entity.Progress

/**
 * Delegate for Progress item in recycler view.
 */
class ProgressAdapterDelegate : AbsListItemAdapterDelegate<Progress, UiEntity, ProgressAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_progress))

    override fun isForViewType(item: UiEntity, items: MutableList<UiEntity>, position: Int) =
        items[position] is Progress

    override fun onBindViewHolder(item: Progress, holder: ViewHolder, payloads: MutableList<Any>) = Unit

    /**
     * ViewHolder for Progress.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
