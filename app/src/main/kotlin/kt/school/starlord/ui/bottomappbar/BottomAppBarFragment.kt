package kt.school.starlord.ui.bottomappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kt.school.starlord.R

/**
 * First fragment that is appear on screen after computation app launch.
 * Such navigation is configured by R.navigation.app_graph.
 *
 * Layout R.layout.fragment_appbar contains BottomAppBar, FloatingActionButton and fragment container with
 * R.navigation.bottom_appbar_graph navigation.
 *
 * @see R.navigation.app_graph
 * @see R.layout.fragment_appbar
 * @see R.navigation.bottom_appbar_graph
 */
class BottomAppBarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_appbar, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, enabled = false) {
            findNavController().navigateUp()
        }
    }
}
