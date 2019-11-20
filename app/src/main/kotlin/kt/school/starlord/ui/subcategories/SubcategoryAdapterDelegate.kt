package kt.school.starlord.ui.subcategories

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_subcategory.view.*
import kt.school.starlord.R
import kt.school.starlord.ui.global.extension.inflate
import kt.school.starlord.ui.subcategories.entity.UiSubcategory
import kotlin.properties.Delegates

/**
 * Delegate for subcategories in recycler view.
 * @param clickListener listener for clicks on subcategory
 */
class SubcategoryAdapterDelegate(
    private val clickListener: (UiSubcategory) -> Unit
) : AbsListItemAdapterDelegate<UiSubcategory, Any, SubcategoryAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_subcategory))

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) = items[position] is UiSubcategory

    override fun onBindViewHolder(item: UiSubcategory, holder: ViewHolder, payloads: MutableList<Any>) =
        holder.bind(item)

    /**
     * ViewHolder for subcategory.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var subcategory: UiSubcategory by Delegates.notNull()

        init {
            view.setOnClickListener { clickListener(subcategory) }
        }

        /**
         * Binds data with view.
         */
        fun bind(item: UiSubcategory) = with(itemView) {
            subcategory = item
            name.text = item.name
            count.visibility = item.countVisibility
            count.text = item.count
        }
    }
}
