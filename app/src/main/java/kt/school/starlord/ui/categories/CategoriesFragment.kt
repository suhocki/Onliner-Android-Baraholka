package kt.school.starlord.ui.categories

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
 * Contains a recycler that is filled by categories.
 * Clicking on category takes you to the subcategories fragment.
 */
class CategoriesFragment : Fragment() {

    private val viewModel: CategoriesViewModel by viewModel()
    private val systemMessageReceiver: SystemMessageReceiver by inject()

    private val adapter by lazy {
        AppRecyclerAdapter(
            CategoryAdapterDelegate {
                val direction = CategoriesFragmentDirections.toSubcategories(it.name)
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadLocalCategories()
            viewModel.loadRemoteCategories()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@CategoriesFragment.adapter
        }

        viewModel.categories.observe(viewLifecycleOwner, Observer(adapter::setData))
        viewModel.progress.observe(viewLifecycleOwner, Observer(systemMessageReceiver::showProgress))
        viewModel.error.observe(viewLifecycleOwner, Observer(systemMessageReceiver::showError))
    }
}
