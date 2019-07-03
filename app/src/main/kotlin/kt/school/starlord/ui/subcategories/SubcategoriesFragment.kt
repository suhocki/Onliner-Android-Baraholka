package kt.school.starlord.ui.subcategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.model.system.SystemMessageReceiver
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contains a recycler that is filled by subcategories.
 * Clicking on subcategory takes you to a products fragment.
 */
class SubcategoriesFragment : Fragment() {

    private val viewModel: SubcategoriesViewModel by viewModel()
    private val systemMessageReceiver: SystemMessageReceiver by inject()
    private val adapter by lazy {
        AppRecyclerAdapter(
            SubcategoryAdapterDelegate {
                val direction = SubcategoriesFragmentDirections.toProducts()
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val safeArgs = SubcategoriesFragmentArgs.fromBundle(requireArguments())
            val categoryName = safeArgs.categoryName
            viewModel.loadSubcategories(categoryName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_subcategories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@SubcategoriesFragment.adapter
        }

        viewModel.getSubcategories().observe(viewLifecycleOwner, Observer(adapter::setData))
        viewModel.getErrors().observe(viewLifecycleOwner, Observer(systemMessageReceiver::showError))
    }
}
