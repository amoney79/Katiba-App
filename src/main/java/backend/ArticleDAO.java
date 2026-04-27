package backend;

import backend.models.Article;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private final MongoCollection<Document> collection;

    public ArticleDAO() {
        collection = DatabaseManager.getInstance().getDatabase().getCollection("articles");
        collection.createIndex(Indexes.text("title"));
        collection.createIndex(Indexes.text("content"));
    }

    // ─── Fetch all articles ────────────────────────────────────────────────────

    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        for (Document doc : collection.find().sort(new Document("chapterNumber", 1).append("articleNumber", 1))) {
            articles.add(docToArticle(doc));
        }
        return articles;
    }

    // ─── Fetch by chapter ─────────────────────────────────────────────────────

    public List<Article> findByChapter(int chapterNumber) {
        List<Article> articles = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("chapterNumber", chapterNumber))
                .sort(new Document("articleNumber", 1))) {
            articles.add(docToArticle(doc));
        }
        return articles;
    }

    // ─── Full-text search ─────────────────────────────────────────────────────

    public List<Article> search(String query) {
        List<Article> articles = new ArrayList<>();
        Document textFilter = new Document("$text", new Document("$search", query));
        for (Document doc : collection.find(textFilter).limit(50)) {
            articles.add(docToArticle(doc));
        }
        return articles;
    }

    // ─── Fetch by ID ──────────────────────────────────────────────────────────

    public Article findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? docToArticle(doc) : null;
    }

    // ─── Get distinct chapter list ────────────────────────────────────────────

    public List<Document> getChapters() {
        List<Document> chapters = new ArrayList<>();
        collection.distinct("chapterNumber", Integer.class).forEach(num -> {
            Document first = collection.find(Filters.eq("chapterNumber", num))
                                       .sort(new Document("articleNumber", 1)).first();
            if (first != null) {
                chapters.add(new Document("chapterNumber", num)
                        .append("chapterTitle", first.getString("chapterTitle")));
            }
        });
        chapters.sort((a, b) -> a.getInteger("chapterNumber") - b.getInteger("chapterNumber"));
        return chapters;
    }

    // ─── Mapping helper ───────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Article docToArticle(Document doc) {
        Article a = new Article();
        a.setId(doc.getObjectId("_id"));
        a.setChapterNumber(doc.getInteger("chapterNumber", 0));
        a.setChapterTitle(doc.getString("chapterTitle"));
        a.setArticleNumber(doc.getInteger("articleNumber", 0));
        a.setTitle(doc.getString("title"));
        a.setContent(doc.getString("content"));
        a.setTags(doc.getList("tags", String.class, List.of()));
        return a;
    }
}
