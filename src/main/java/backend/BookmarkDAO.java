package backend;

import backend.models.Article;
import backend.models.Bookmark;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookmarkDAO {

    private final MongoCollection<Document> collection;

    public BookmarkDAO() {
        collection = DatabaseManager.getInstance().getDatabase().getCollection("bookmarks");
    }

    public List<Bookmark> findAll() {
        List<Bookmark> list = new ArrayList<>();
        for (Document doc : collection.find().sort(new Document("savedAt", -1))) {
            list.add(docToBookmark(doc));
        }
        return list;
    }

    public boolean isBookmarked(ObjectId articleId) {
        return collection.find(Filters.eq("articleId", articleId)).first() != null;
    }

    public Bookmark save(Article article) {
        // Prevent duplicates
        Document existing = collection.find(Filters.eq("articleId", article.getId())).first();
        if (existing != null) return docToBookmark(existing);

        Bookmark bm = new Bookmark(article);
        Document doc = new Document()
                .append("articleId",     bm.getArticleId())
                .append("articleNumber", bm.getArticleNumber())
                .append("articleTitle",  bm.getArticleTitle())
                .append("chapterNumber", bm.getChapterNumber())
                .append("chapterTitle",  bm.getChapterTitle())
                .append("note",          bm.getNote())
                .append("savedAt",       new Date());

        collection.insertOne(doc);
        bm.setId(doc.getObjectId("_id"));
        return bm;
    }

    public void delete(ObjectId bookmarkId) {
        collection.deleteOne(Filters.eq("_id", bookmarkId));
    }

    public void deleteByArticle(ObjectId articleId) {
        collection.deleteOne(Filters.eq("articleId", articleId));
    }

    public void updateNote(ObjectId bookmarkId, String note) {
        collection.updateOne(Filters.eq("_id", bookmarkId),
                new Document("$set", new Document("note", note)));
    }

    private Bookmark docToBookmark(Document doc) {
        Bookmark bm = new Bookmark();
        bm.setId(doc.getObjectId("_id"));
        bm.setArticleId(doc.getObjectId("articleId"));
        bm.setArticleNumber(doc.getInteger("articleNumber", 0));
        bm.setArticleTitle(doc.getString("articleTitle"));
        bm.setChapterNumber(doc.getInteger("chapterNumber", 0));
        bm.setChapterTitle(doc.getString("chapterTitle"));
        bm.setNote(doc.getString("note"));
        Date d = doc.getDate("savedAt");
        if (d != null) bm.setSavedAt(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return bm;
    }
}
