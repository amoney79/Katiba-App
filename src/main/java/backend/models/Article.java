package backend.models;

import org.bson.types.ObjectId;
import java.util.List;

public class Article {

    private ObjectId id;
    private int chapterNumber;
    private String chapterTitle;
    private int articleNumber;
    private String title;
    private String content;
    private List<String> tags;

    public Article() {}

    public Article(int chapterNumber, String chapterTitle, int articleNumber,
                   String title, String content, List<String> tags) {
        this.chapterNumber = chapterNumber;
        this.chapterTitle  = chapterTitle;
        this.articleNumber = articleNumber;
        this.title         = title;
        this.content       = content;
        this.tags          = tags;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public ObjectId getId()            { return id; }
    public void setId(ObjectId id)     { this.id = id; }

    public int getChapterNumber()               { return chapterNumber; }
    public void setChapterNumber(int n)         { this.chapterNumber = n; }

    public String getChapterTitle()             { return chapterTitle; }
    public void setChapterTitle(String t)       { this.chapterTitle = t; }

    public int getArticleNumber()               { return articleNumber; }
    public void setArticleNumber(int n)         { this.articleNumber = n; }

    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    public String getContent()                  { return content; }
    public void setContent(String content)      { this.content = content; }

    public List<String> getTags()               { return tags; }
    public void setTags(List<String> tags)      { this.tags = tags; }

    @Override
    public String toString() {
        return "Article " + articleNumber + ": " + title;
    }
}
