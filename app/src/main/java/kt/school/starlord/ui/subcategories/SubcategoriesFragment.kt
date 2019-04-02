package kt.school.starlord.ui.subcategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.entity.Category
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.ui.global.CategoryAdapterDelegate
import org.jetbrains.anko.support.v4.toast

class SubcategoriesFragment: Fragment() {

    private val adapter by lazy { SubcategoriesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = SubcategoriesFragmentArgs.fromBundle(it)
            val subcategories = listOf(Subcategory("name", 15, "link"))
            adapter.setData(listOf(Category(safeArgs.categoryName, subcategories)))
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subcategories, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@SubcategoriesFragment.adapter
        }
    }

    private inner class SubcategoriesAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(CategoryAdapterDelegate { toast("$it") })
        }

        fun setData(data: List<Any>) {
            items.clear()
            items.addAll(data)

            notifyDataSetChanged()
        }
    }
}