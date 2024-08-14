package tj.donishomuz.megafonapk.adapters

import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.models.Article

class ViewPagerAdapter() : RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>() {

    private lateinit var vptext: TextView
    private lateinit var time: TextView

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }
    private var onItemClickListener: ((Article) -> Unit)? = null


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(differ.currentList[position].urlToImage.toString())
        vptext = holder.itemView.findViewById(R.id.vp_text)
        time = holder.itemView.findViewById(R.id.time_vp)
        vptext.text = differ.currentList[position].title
        time.text = differ.currentList[position].publishedAt?.substring(0,10)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(article)
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image_view)

        fun bind(imageUrl: String) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.news)
                .error(R.drawable.news)
                .into(imageView)

        }
    }
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}

