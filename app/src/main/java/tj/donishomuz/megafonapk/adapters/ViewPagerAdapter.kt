package tj.donishomuz.megafonapk.adapters

import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.models.Article

class ViewPagerAdapter(
    private val images: List<Article>
) : RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>() {

    private lateinit var vptext: TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position].urlToImage.toString())
        vptext = holder.itemView.findViewById(R.id.vp_text)
        vptext.text = images[position].title

    }

    override fun getItemCount(): Int = images.size
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image_view)

        fun bind(imageUrl: String) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(imageView)
        }
    }
}

