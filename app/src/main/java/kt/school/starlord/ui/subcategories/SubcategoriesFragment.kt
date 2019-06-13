package kt.school.starlord.ui.subcategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.ui.global.SubcategoryAdapterDelegate
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubcategoriesFragment : Fragment() {

    private val viewModel: SubcategoriesViewModel by viewModel()

    private val adapter by lazy { SubcategoriesAdapter() }

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

        val safeArgs = SubcategoriesFragmentArgs.fromBundle(requireArguments())
        val categoryName = safeArgs.categoryName
        viewModel.getSubcategoriesLiveData(categoryName).observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }

    private inner class SubcategoriesAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(SubcategoryAdapterDelegate { toast("$it") })
        }

        fun setData(data: List<Any>) {
            items.clear()
            items.addAll(data)

            notifyDataSetChanged()
        }
    }
}