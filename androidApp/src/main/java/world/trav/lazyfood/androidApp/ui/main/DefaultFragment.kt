package world.trav.lazyfood.androidApp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.imagepicker.features.ImagePicker
import timber.log.Timber
import world.trav.lazyfood.androidApp.databinding.DefaultFragmentBinding

class DefaultFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: DefaultFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DefaultFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            Timber.d("on click")
            ImagePicker.create(DefaultFragment@this).start()
        }
    }

}