package frontend;

import backend.BookmarkDAO;
import backend.models.Article;
import backend.models.Bookmark;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ArticleDetailPane {

    private final BorderPane root;
    private final MainWindow  nav;
    private final BookmarkDAO bookmarkDAO;

    private Article currentArticle;

    // Dynamic nodes updated on load()
    private final Label  chapterLabel = new Label();
    private final Label  titleLabel   = new Label();
    private final Label  contentLabel = new Label();
    private final Button bookmarkBtn  = new Button("☆  Bookmark");
    private final Label  tagsLabel    = new Label();

    public ArticleDetailPane(MainWindow nav, BookmarkDAO bookmarkDAO) {
        this.nav         = nav;
        this.bookmarkDAO = bookmarkDAO;

        chapterLabel.getStyleClass().add("card-chapter-num");
        titleLabel.getStyleClass().add("article-title");
        titleLabel.setWrapText(true);
        contentLabel.getStyleClass().add("article-content");
        contentLabel.setWrapText(true);
        tagsLabel.getStyleClass().add("result-snippet");

        bookmarkBtn.getStyleClass().add("bookmark-btn");
        bookmarkBtn.setOnAction(e -> toggleBookmark());

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> nav.showHome());

        HBox topBar = new HBox(12, backBtn, new Spacer(), bookmarkBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("detail-topbar");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2a2a3e;");

        VBox body = new VBox(16, topBar, chapterLabel, titleLabel, sep, contentLabel, tagsLabel);
        body.setPadding(new Insets(32));
        body.setMaxWidth(820);

        StackPane centerer = new StackPane(body);
        centerer.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(centerer);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");

        root = new BorderPane(scroll);
        root.getStyleClass().add("content-pane");
    }

    public void load(Article article) {
        this.currentArticle = article;

        chapterLabel.setText("Chapter " + article.getChapterNumber() + " — " + article.getChapterTitle());
        titleLabel.setText("Article " + article.getArticleNumber() + ": " + article.getTitle());
        contentLabel.setText(article.getContent() != null ? article.getContent() : "Content not available.");

        if (article.getTags() != null && !article.getTags().isEmpty()) {
            tagsLabel.setText("Tags: " + String.join(" • ", article.getTags()));
        } else {
            tagsLabel.setText("");
        }

        // Update bookmark button state
        if (article.getId() != null && bookmarkDAO.isBookmarked(article.getId())) {
            bookmarkBtn.setText("★  Bookmarked");
            bookmarkBtn.getStyleClass().add("bookmarked");
        } else {
            bookmarkBtn.setText("☆  Bookmark");
            bookmarkBtn.getStyleClass().remove("bookmarked");
        }
    }

    private void toggleBookmark() {
        if (currentArticle == null) return;

        if (currentArticle.getId() != null && bookmarkDAO.isBookmarked(currentArticle.getId())) {
            bookmarkDAO.deleteByArticle(currentArticle.getId());
            bookmarkBtn.setText("☆  Bookmark");
            bookmarkBtn.getStyleClass().remove("bookmarked");
        } else {
            bookmarkDAO.save(currentArticle);
            bookmarkBtn.setText("★  Bookmarked");
            bookmarkBtn.getStyleClass().add("bookmarked");
        }
    }

    // Simple spacer helper
    private static final class Spacer extends Region {
        Spacer() { HBox.setHgrow(this, Priority.ALWAYS); }
    }

    public Node getRoot() { return root; }
}
