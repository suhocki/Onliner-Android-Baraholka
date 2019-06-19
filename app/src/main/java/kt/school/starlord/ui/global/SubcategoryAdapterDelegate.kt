package kt.school.starlord.ui.global

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_subcategory.view.*
import kt.school.starlord.R
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.extension.inflate

/**
 * Delegate for subcategories in recycler view
 * @param clickListener listener for clicks on subcategory
 */
class SubcategoryAdapterDelegate(
    private val clickListener: (Subcategory) -> Unit
) : AbsListItemAdapterDelegate<Subcategory, Any, SubcategoryAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(parent.inflate(R.layout.item_subcategory))

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ) = items[position] is Subcategory

    override fun onBindViewHolder(
        item: Subcategory,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    /**
     * ViewHolder for subcategory
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var subcategory: Subcategory

        init {
            view.setOnClickListener { clickListener(subcategory) }
        }

        /**
         * Binds data with view
         */
        fun bind(item: Subcategory) = with(itemView) {
            subcategory = item
            name.text = item.name
            count.text = item.count.toString()
        }
    }
}