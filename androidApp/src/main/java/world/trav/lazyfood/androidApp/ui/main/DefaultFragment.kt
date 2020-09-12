package world.trav.lazyfood.androidApp.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.leochuan.CenterSnapHelper
import com.leochuan.ScaleLayoutManager
import com.leochuan.ScrollHelper
import com.leochuan.ViewPagerLayoutManager
import timber.log.Timber
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.androidApp.databinding.DefaultFragmentBinding
import world.trav.lazyfood.androidApp.databinding.GalleryItemBinding
import kotlin.math.roundToInt


class DefaultFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: DefaultFragmentBinding
    private var rotateCarousel: Boolean = false
    private lateinit var scaleLayoutManager: MyScaleLayoutManager
    private lateinit var handler: Handler
    private val timeInterval = 100L
    private val autoPlayRunnable = object : Runnable {
        override fun run() {
            val currentPosition =
                scaleLayoutManager.getCurrentPositionOffset() * if (scaleLayoutManager.reverseLayout) -1 else 1
            val delta = scaleLayoutManager.getOffsetToPosition(currentPosition + 1)
            binding.recyclerView.smoothScrollBy(delta, 0)
            handler.postDelayed(this, timeInterval)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DefaultFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    //until thumb up and thumb down
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Timber.d("create")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        scaleLayoutManager = MyScaleLayoutManager(requireContext(), dp2px(requireContext(), 1f))

        val resource = arrayListOf(
            R.drawable.food1,
            R.drawable.food2,
            R.drawable.food3,
            R.drawable.food4,
            R.drawable.food5,
        )

        binding.message.text = "${resource.size} foods selection"

        scaleLayoutManager.minAlpha = 0.2f
        scaleLayoutManager.infinite = true
        scaleLayoutManager.moveSpeed = 5f
        binding.recyclerView.layoutManager = scaleLayoutManager
        binding.recyclerView.adapter = GalleryAdapter(this, resource)

        binding.fab.setOnClickListener {
            if (!rotateCarousel) {
                rotateCarousel = true
                binding.fab.setImageResource(R.drawable.stop_24px)
                handler.postDelayed(autoPlayRunnable, timeInterval)
            } else {
                rotateCarousel = false
                binding.fab.setImageResource(R.drawable.play_arrow_24px)
                handler.removeCallbacks(autoPlayRunnable)
            }
        }

        CenterSnapHelper().attachToRecyclerView(binding.recyclerView)
    }

    private fun dp2px(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Timber.d("create menu")
        inflater.inflate(R.menu.default_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.action_add -> {
                Timber.d("on add food photo")
                ImagePicker.create(this).start()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MyScaleLayoutManager(context: Context, itemSpace: Int) : ScaleLayoutManager(
        context,
        itemSpace
    ) {
        fun getCurrentPositionOffset(): Int {
            if (mInterval.toInt() == 0) return 0;
            return (mOffset / mInterval).roundToInt();
        }
    }

    class GalleryAdapter(private val fragment: Fragment, private val urls: List<Int>) :
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        private lateinit var binding: GalleryItemBinding;

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding = GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding.root)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Timber.d("loading " + urls[position])
            Glide.with(fragment).load(urls[position]).into(binding.imageView)
        }

        override fun getItemCount(): Int {
            return urls.size
        }

    }
}