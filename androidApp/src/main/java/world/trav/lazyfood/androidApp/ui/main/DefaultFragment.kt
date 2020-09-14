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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import timber.log.Timber
import world.trav.lazyfood.androidApp.BuildConfig
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
    private lateinit var foods: Foods
    private lateinit var galleryAdapter: GalleryAdapter
    private var stopping: Boolean = false
    private var stoppingCount: Int = Int.MAX_VALUE

    private val autoPlayRunnable = object : Runnable {
        override fun run() {
            if (stoppingCount > 0) {
                val currentPosition =
                    scaleLayoutManager.getCurrentPositionOffset() * if (scaleLayoutManager.reverseLayout) -1 else 1
                val delta = scaleLayoutManager.getOffsetToPosition(currentPosition + 1)
                Timber.d("current position $currentPosition, currentIndex = ${scaleLayoutManager.currentPosition}, delta " + delta)
                binding.recyclerView.smoothScrollBy(delta, 0)
                handler.postDelayed(this, timeInterval)
                stoppingCount--
            } else {
                Timber.d("stopping autoplay")
                stopping = false
                rotateCarousel = false
                toggleButtons(true)
                handler.removeCallbacks(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DefaultFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

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

        val foodList = arrayListOf(
            Food(R.drawable.food1),
            Food(R.drawable.food2),
            Food(R.drawable.food3),
            Food(R.drawable.food4),
            Food(R.drawable.food5),
        )

        foods = Foods(foodList)
        galleryAdapter = GalleryAdapter(this, foodList)

        if (BuildConfig.DEBUG) {
            binding.message.text = getString(R.string.food_selection, foodList.size.toString())
        } else {
            binding.message.visibility = View.GONE
        }

        scaleLayoutManager.minAlpha = 0.2f
        scaleLayoutManager.infinite = true
        scaleLayoutManager.moveSpeed = 5f
        binding.recyclerView.layoutManager = scaleLayoutManager
        binding.recyclerView.adapter = galleryAdapter
        CenterSnapHelper().attachToRecyclerView(binding.recyclerView)

        binding.fab.setOnClickListener {

            if (stopping) return@setOnClickListener

            if (!rotateCarousel) {
                rotateCarousel = true
                stoppingCount = Int.MAX_VALUE
                toggleButtons(false)
                handler.postDelayed(autoPlayRunnable, timeInterval)
            } else {

                if (!stopping) {
                    stopping = true
                    stoppingCount = foods.nextStopCount(scaleLayoutManager.currentPosition)
                }
            }
        }

        binding.thumbDown.setOnClickListener {
            foods.voteDown(scaleLayoutManager.currentPosition)
            Timber.d(
                "thumbDown ${scaleLayoutManager.currentPosition}, foods[${scaleLayoutManager.currentPosition}].weight = ${
                    foods.get(
                        scaleLayoutManager.currentPosition
                    ).weight
                }"
            )
            binding.emoji.setImageResource(R.drawable.sentiment_dissatisfied_24px)
            binding.emoji.alpha = 1f
            binding.emoji.animate().alpha(0f).setDuration(2000).setListener(null)
        }

        binding.thumbUp.setOnClickListener {
            foods.voteUp(scaleLayoutManager.currentPosition)
            Timber.d(
                "thumbUp ${scaleLayoutManager.currentPosition}, foods[${scaleLayoutManager.currentPosition}].weight = ${
                    foods.get(
                        scaleLayoutManager.currentPosition
                    ).weight
                }"
            )
            binding.emoji.setImageResource(R.drawable.sentiment_satisfied_24px)
            binding.emoji.alpha = 1f
            binding.emoji.animate().alpha(0f).setDuration(2000).setListener(null)
        }
    }

    private fun toggleButtons(run: Boolean) {
        if (run) {
            binding.thumbDown.isEnabled = run
            binding.thumbDown.alpha = 1f
            binding.thumbUp.isEnabled = run
            binding.thumbUp.alpha = 1f
            binding.fab.setImageResource(R.drawable.play_arrow_24px)
        } else {
            binding.thumbDown.isEnabled = run
            binding.thumbDown.alpha = 0.5f
            binding.thumbUp.isEnabled = run
            binding.thumbUp.alpha = 0.5f
            binding.fab.setImageResource(R.drawable.stop_24px)
        }
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

    class GalleryAdapter(private val fragment: Fragment, private val foods: List<Food>) :
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
            Timber.d("loading food image: ${foods[position].resourceId}")
            binding.weight.text = "food $position"
            Glide.with(fragment).load(foods[position].resourceId).into(binding.imageView)
        }

        override fun getItemCount(): Int {
            return foods.size
        }
    }
}