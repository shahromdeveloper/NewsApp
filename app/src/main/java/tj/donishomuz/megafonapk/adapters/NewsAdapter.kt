package tj.donishomuz.megafonapk.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.models.Article
import tj.donishomuz.megafonapk.ui.NewsViewModel

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var articleImage: ImageView

    //private lateinit var articleSource: TextView
    private lateinit var articleTitle: TextView
    private lateinit var articleDateTime: TextView

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        articleImage = holder.itemView.findViewById(R.id.articleImage)
        articleTitle = holder.itemView.findViewById(R.id.articleTitle)
        articleDateTime = holder.itemView.findViewById(R.id.articleDateTime)


     //   Log.d("TESTTTT", article.urlToImage.toString())

        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .placeholder(R.drawable.news)
                .error(R.drawable.news)
                .into(articleImage)

            //articleSource.text = article.source?.name
            articleTitle.text = article.title
            articleDateTime.text = article.publishedAt?.substring(0, 10)
           // articleContent.text = article.content

            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }

        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

}















