package tj.donishomuz.megafonapk.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.databinding.FragmentArticleBinding
import tj.donishomuz.megafonapk.models.Article
import tj.donishomuz.megafonapk.ui.NewsActivity
import tj.donishomuz.megafonapk.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding
    private lateinit var titleText: TextView
    //private lateinit var sourceText: TextView
    private lateinit var dateTimeText: TextView
    private lateinit var contentText: TextView
    //private lateinit var authorText: TextView
    private lateinit var articleImage: ImageView
    private lateinit var bookmarkButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        titleText = view.findViewById(R.id.articleTitle)
       // sourceText = view.findViewById(R.id.articleSource)
        dateTimeText = view.findViewById(R.id.articleDateTime)
        contentText = view.findViewById(R.id.articleContent)
        //authorText = view.findViewById(R.id.articleAuthor)
        articleImage = view.findViewById(R.id.articleImage)
        bookmarkButton = view.findViewById(R.id.btnBookmark)

        newsViewModel = (activity as NewsActivity).newsViewModel
        var article = args.article

        setContent(args.article)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnBookmark.setOnClickListener {
            if (newsViewModel.isArticleSaved.value == true) {
                newsViewModel.deleteArticleByPublishedAt(article.publishedAt ?: "")
                Snackbar.make(view, "Remove Success", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.addNewsToFavorites(article)
                    }
                    show()
                }
            } else {
                newsViewModel.addNewsToFavorites(article)
                Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_SHORT).show()
            }
        }

        newsViewModel.getFavoriteNewsByPublishedAt(article.publishedAt ?: "")

        newsViewModel.isArticleSaved.observe(viewLifecycleOwner) { status ->
            if (status) {
                bookmarkButton.setImageResource(R.drawable.baseline_bookmark_24)
            } else {
                bookmarkButton.setImageResource(R.drawable.ic_bookmark)
            }
        }
    }

    private fun setContent(article: Article) {
        titleText.text = article.title
      //  sourceText.text = article.source?.name ?: ""
        dateTimeText.text = article.publishedAt?.substring(0, 10)
        contentText.text = article.description
      //  authorText.text = article.author

        Glide.with(this)
            .load(article.urlToImage)
            .placeholder(R.drawable.image_newspaper)
            .error(R.drawable.image_newspaper)
            .into(articleImage)
    }

}