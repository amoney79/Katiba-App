package backend.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Bookmark {

    private ObjectId id;
    private ObjectId articleId;
    private int articleNumber;
    private String articleTitle;
    private int chapterNumber;
    private String chapterTitle;
    private String note;
    private LocalDateTime savedAt;

    public Bookmark() {}

    public Bookmark(Article article) {
        this.articleId     = article.getId();
        this.articleNumber = article.getArticleNumber();
        this.articleTitle  = article.getTitle();
        this.chapterNumber = article.getChapterNumber();
        this.chapterTitle  = article.getChapterTitle();
        this.savedAt       = LocalDateTime.now();
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public ObjectId getId()                       { return id; }
    public void setId(ObjectId id)                { this.id = id; }

    public ObjectId getArticleId()                { return articleId; }
    public void setArticleId(ObjectId articleId)  { this.articleId = articleId; }

    public int getArticleNumber()                 { return articleNumber; }
    public void setArticleNumber(int n)           { this.articleNumber = n; }

    public String getArticleTitle()               { return articleTitle; }
    public void setArticleTitle(String t)         { this.articleTitle = t; }

    public int getChapterNumber()                 { return chapterNumber; }
    public void setChapterNumber(int n)           { this.chapterNumber = n; }

    public String getChapterTitle()               { return chapterTitle; }
    public void setChapterTitle(String t)         { this.chapterTitle = t; }

    public String getNote()                       { return note; }
    public void setNote(String note)              { this.note = note; }

    public LocalDateTime getSavedAt()             { return savedAt; }
    public void setSavedAt(LocalDateTime d)       { this.savedAt = d; }
}
