package kt.school.starlord.ui.global

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class AppRecyclerAdapter(
    vararg adapterDelegates: AdapterDelegate<MutableList<Any>>
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        adapterDelegates.forEach { delegatesManager.addDelegate(it) }
    }

    fun setData(data: List<Any>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}
