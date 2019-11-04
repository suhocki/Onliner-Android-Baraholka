package kt.school.starlord.ui.global

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.paging.PagedListDelegationAdapter
import kt.school.starlord.ui.global.entity.UiEntity

/**
 * Common recycler adapter with pagination.
 */
class AppPagedRecyclerAdapter(
    vararg adapterDelegates: AdapterDelegate<MutableList<UiEntity>>
) : PagedListDelegationAdapter<UiEntity>(

    object : DiffUtil.ItemCallback<UiEntity>() {
        override fun areItemsTheSame(oldItem: UiEntity, newItem: UiEntity) =
            oldItem.isItemTheSame(newItem)

        override fun areContentsTheSame(oldItem: UiEntity, newItem: UiEntity) =
            oldItem.isContentTheSame(newItem)

        override fun getChangePayload(oldItem: UiEntity, newItem: UiEntity) =
            Any()
    },
    *adapterDelegates
)
