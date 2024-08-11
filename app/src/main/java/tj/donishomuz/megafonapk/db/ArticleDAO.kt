package tj.donishomuz.megafonapk.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tj.donishomuz.megafonapk.models.Article

const val ARTICLES_TABLE = "articles"

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM $ARTICLES_TABLE")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM $ARTICLES_TABLE WHERE publishedAt = :publishedAt LIMIT 1")
    fun getArticleByPublishedAt(publishedAt: String): LiveData<Article>

    @Delete
    suspend fun deleteArticle(article: Article): Int

    @Query("DELETE FROM $ARTICLES_TABLE WHERE publishedAt = :publishedAt")
    suspend fun deleteArticleByPublishedAt(publishedAt: String): Int
}