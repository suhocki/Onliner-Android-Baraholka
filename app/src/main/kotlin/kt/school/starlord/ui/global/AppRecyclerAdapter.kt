package kt.school.starlord.ui.global

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

/**
 * Common recycler adapter.
 */
class AppRecyclerAdapter(
    vararg adapterDelegates: AdapterDelegate<MutableList<Any>>
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        adapterDelegates.forEach { delegatesManager.addDelegate(it) }
    }

    /**
     * Replace existing data with a new one.
     */
    fun setData(data: List<Any>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}
