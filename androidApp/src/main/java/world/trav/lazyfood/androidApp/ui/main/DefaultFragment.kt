package world.trav.lazyfood.androidApp.ui.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.leochuan.CenterSnapHelper
import com.leochuan.ScaleLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import world.trav.lazyfood.androidApp.BuildConfig
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.androidApp.databinding.DefaultFragmentBinding
import world.trav.lazyfood.androidApp.databinding.GalleryItemBinding
import world.trav.lazyfood.androidApp.utils.fadeIn
import world.trav.lazyfood.androidApp.utils.fadeOut
import world.trav.lazyfood.shared.Food
import world.trav.lazyfood.shared.Foods
import world.trav.lazyfood.shared.Foods.Companion.GROUP_SIZE
import kotlin.math.roundToInt

@AndroidEntryPoint
class DefaultFragment : Fragment() {

    private lateinit var foodList: ArrayList<Food>
    private lateinit var binding: DefaultFragmentBinding
    private var rotateCarousel: Boolean = false
    private lateinit var scaleLayoutManager: MyScaleLayoutManager
    private lateinit var handler: Handler
    private val timeInterval = 100L
    private lateinit var galleryAdapter: GalleryAdapter
    private var stopping: Boolean = false
    private var stopIndex: Int = Int.MIN_VALUE
    private val viewModel by viewModels<DefaultViewModel>()

    private val autoPlayRunnable = object : Runnable {
        override fun run() {
            if (stopIndex != scaleLayoutManager.currentPosition) {
                val currentPosition =
                    scaleLayoutManager.getCurrentPositionOffset() * if (scaleLayoutManager.reverseLayout) -1 else 1
                val delta = scaleLayoutManager.getOffsetToPosition(currentPosition + 1)
                Timber.d("current position $currentPosition, currentIndex = ${scaleLayoutManager.currentPosition}, delta " + delta)
                binding.content.recyclerView.smoothScrollBy(delta, 0)
                handler.postDelayed(this, timeInterval)
            } else {
                Timber.d(
                    "stopping autoplay, stopped at index ${scaleLayoutManager.currentPosition}, weight ${
                        viewModel.getFoodWeight(scaleLayoutManager.currentPosition)
                    }"
                )
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFoods().observe(viewLifecycleOwner, { it ->
            it?.let {
                initData(it)
            }
        })

        viewModel.getLastAddedFood().observe(viewLifecycleOwner, { food ->
            food?.let {
                foodList.add(0, food)
                Timber.d("food list size: ${foodList.size}")
                setupRecyclerView()
                viewModel.clearLastAddedFood()
            }
        })

        viewModel.getLastRemovedFood().observe(viewLifecycleOwner, { food ->
            food?.let {
                foodList.remove(food)
                Timber.d("food list size: ${foodList.size}")
                setupRecyclerView()
                viewModel.clearLastRemovedFood()
            }
        })

        handler = Handler(Looper.getMainLooper())

        binding.content.container.visibility = View.GONE
        binding.loading.container.visibility = View.VISIBLE

        binding.content.fab.setOnClickListener {

            if (stopping) return@setOnClickListener

            if (!rotateCarousel) {
                rotateCarousel = true
                stopIndex = Int.MIN_VALUE
                toggleButtons(false)
                handler.postDelayed(autoPlayRunnable, timeInterval)
            } else if (!stopping) {
                stopping = true
                stopIndex = viewModel.getNextStopIndex(scaleLayoutManager.currentPosition)
            }
        }

        binding.content.thumbDown.setOnClickListener {
            viewModel.voteFoodDown(scaleLayoutManager.currentPosition)
            Timber.d(
                "thumbDown ${scaleLayoutManager.currentPosition}, foods[${scaleLayoutManager.currentPosition}].weight = ${
                    viewModel.getFoodWeight(scaleLayoutManager.currentPosition)
                }"
            )
            binding.content.emoji.setImageResource(R.drawable.sentiment_dissatisfied_24px)
            binding.content.emoji.alpha = 1f
            binding.content.emoji.animate().alpha(0f).setDuration(2000).setListener(null)
        }

        binding.content.thumbUp.setOnClickListener {
            viewModel.voteFoodUp(scaleLayoutManager.currentPosition)
            Timber.d(
                "thumbUp ${scaleLayoutManager.currentPosition}, foods[${scaleLayoutManager.currentPosition}].weight = ${
                    viewModel.getFoodWeight(scaleLayoutManager.currentPosition)
                }"
            )
            binding.content.emoji.setImageResource(R.drawable.sentiment_satisfied_24px)
            binding.content.emoji.alpha = 1f
            binding.content.emoji.animate().alpha(0f).setDuration(2000).setListener(null)
        }
    }

    private fun setupRecyclerView() {
        scaleLayoutManager = createLayoutManager()
        binding.content.recyclerView.layoutManager = scaleLayoutManager
        galleryAdapter = GalleryAdapter(this, foodList, viewModel)
        binding.content.recyclerView.adapter = galleryAdapter
    }

    private fun initData(fList: List<Food>) {
        this.foodList = ArrayList(fList)

        setupRecyclerView()
        CenterSnapHelper().attachToRecyclerView(binding.content.recyclerView)

        if (BuildConfig.DEBUG) {
            binding.content.message.text =
                getString(R.string.food_selection, fList.size.toString())
        } else {
            binding.content.message.visibility = View.GONE
        }

        binding.loading.container.fadeOut()
        binding.content.container.fadeIn()
    }

    private fun createLayoutManager(): MyScaleLayoutManager {
        return MyScaleLayoutManager(requireContext(), dp2px(requireContext(), 1f)).also {
            it.minAlpha = 0.2f
            it.infinite = true
            it.moveSpeed = 5f
        }
    }

    private fun toggleButtons(run: Boolean) {
        if (run) {
            binding.content.thumbDown.isEnabled = run
            binding.content.thumbDown.alpha = 1f
            binding.content.thumbUp.isEnabled = run
            binding.content.thumbUp.alpha = 1f
            binding.content.fab.setImageResource(R.drawable.play_arrow_24px)
        } else {
            binding.content.thumbDown.isEnabled = run
            binding.content.thumbDown.alpha = 0.5f
            binding.content.thumbUp.isEnabled = run
            binding.content.thumbUp.alpha = 0.5f
            binding.content.fab.setImageResource(R.drawable.stop_24px)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Timber.d("image return ${ImagePicker.getFirstImageOrNull(data)}")
            val image = ImagePicker.getFirstImageOrNull(data)

            image?.let {
                context?.let {
                    viewModel.addFoodByUri(it, image.uri)
                }
            }
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

    class GalleryAdapter(
        private val fragment: Fragment,
        private val foods: ArrayList<Food>,
        private val viewModel: DefaultViewModel
    ) :
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

            if (BuildConfig.DEBUG) {
                binding.weight.text = "food $position"
            }

            foods[position].resourceId?.let {
                Glide.with(fragment).load(it).into(binding.imageView)
            } ?: Glide.with(fragment).load(foods[position].imagePath).into(binding.imageView)

            binding.delete.setOnClickListener {
                if (foods[position].isDefault) {
                    val builder = AlertDialog.Builder(fragment.requireContext())
                    builder.setMessage(
                        fragment.getString(
                            R.string.the_image_will_be_automatically_removed,
                            GROUP_SIZE.toString()
                        )
                    ).setTitle(R.string.info)
                    builder.setPositiveButton(R.string.ok) { dialog, id -> dialog.dismiss() }
                    builder.create().show()
                } else {
                    viewModel.deleteFood(foods[position])
                }
            }
        }

        override fun getItemCount(): Int {
            return foods.size
        }
    }
}