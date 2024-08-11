package tj.donishomuz.megafonapk.repository.local

import tj.donishomuz.megafonapk.db.ArticleDatabase
import tj.donishomuz.megafonapk.models.Article

class NewsLocalRepository(private val db: ArticleDatabase) {

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getFavoriteNews() = db.getArticleDao().getAllArticles()

    fun getFavoriteNewsByPublishedAt(publishedAt: String) =
        db.getArticleDao().getArticleByPublishedAt(publishedAt)

    suspend fun deleteArticle(article: Article): Int = db.getArticleDao().deleteArticle(article)

    suspend fun deleteArticleByPublishedAt(publishedAt: String): Int =
        db.getArticleDao().deleteArticleByPublishedAt(publishedAt)
}