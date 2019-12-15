package kt.school.starlord.ui.products.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kt.school.starlord.R
import kt.school.starlord.ui.global.entity.UiEntity
import kt.school.starlord.ui.global.extension.inflate
import kt.school.starlord.ui.products.entity.LoadMore

/**
 * Delegate for loadMore item in recycler view.
 * @param clickListener listener for clicks on loadMore item.
 */
class LoadMoreAdapterDelegate(
    private val clickListener: () -> Unit
) : AbsListItemAdapterDelegate<LoadMore, UiEntity, LoadMoreAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_load_more))

    override fun isForViewType(item: UiEntity, items: MutableList<UiEntity>, position: Int) =
        items[position] is LoadMore

    override fun onBindViewHolder(item: LoadMore, holder: ViewHolder, payloads: MutableList<Any>) = Unit

    /**
     * ViewHolder for loadMore.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { clickListener.invoke() }
        }
    }
}
