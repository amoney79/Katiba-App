package frontend;

import backend.ArticleDAO;
import backend.models.Article;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.bson.Document;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomePane {

    private final BorderPane root;
    private final MainWindow nav;
    private final ArticleDAO articleDAO;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "katiba-loader");
        t.setDaemon(true);
        return t;
    });

    public HomePane(MainWindow nav, ArticleDAO articleDAO) {
        this.nav        = nav;
        this.articleDAO = articleDAO;

        root = new BorderPane();
        root.getStyleClass().add("content-pane");

        // Header
        Label title = new Label("Kenya Constitution 2010");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Browse all 18 Chapters • 260 Articles");
        subtitle.getStyleClass().add("page-subtitle");
        VBox header = new VBox(4, title, subtitle);
        header.getStyleClass().add("page-header");
        root.setTop(header);

        // Chapter grid (loaded async)
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize(48, 48);
        StackPane loading = new StackPane(spinner);
        root.setCenter(loading);

        executor.submit(() -> {
            List<Document> chapters = articleDAO.getChapters();
            Platform.runLater(() -> root.setCenter(buildChapterGrid(chapters)));
        });
    }

    private ScrollPane buildChapterGrid(List<Document> chapters) {
        FlowPane grid = new FlowPane();
        grid.getStyleClass().add("chapter-grid");
        grid.setHgap(16);
        grid.setVgap(16);
        grid.setPadding(new Insets(24));

        for (Document ch : chapters) {
            int    num   = ch.getInteger("chapterNumber");
            String title = ch.getString("chapterTitle");
            grid.getChildren().add(buildChapterCard(num, title));
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        return scroll;
    }

    private VBox buildChapterCard(int num, String title) {
        Label numLabel   = new Label("Chapter " + num);
        numLabel.getStyleClass().add("card-chapter-num");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-chapter-title");
        titleLabel.setWrapText(true);

        Button viewBtn = new Button("View Articles →");
        viewBtn.getStyleClass().add("card-btn");
        viewBtn.setOnAction(e -> loadChapter(num));

        VBox card = new VBox(8, numLabel, titleLabel, viewBtn);
        card.getStyleClass().add("chapter-card");
        card.setPrefWidth(240);
        card.setAlignment(Pos.TOP_LEFT);

        return card;
    }

    private void loadChapter(int chapterNumber) {
        executor.submit(() -> {
            List<Article> articles = articleDAO.findByChapter(chapterNumber);
            Platform.runLater(() -> root.setCenter(buildArticleList(articles)));
        });
    }

    private ScrollPane buildArticleList(List<Article> articles) {
        if (articles.isEmpty()) {
            Label empty = new Label("No articles found.");
            empty.getStyleClass().add("empty-label");
            return new ScrollPane(new StackPane(empty));
        }

        String chTitle = articles.get(0).getChapterTitle();

        // Back button
        Button back = new Button("← Back to Chapters");
        back.getStyleClass().add("back-btn");
        back.setOnAction(e -> {
            executor.submit(() -> {
                List<Document> chs = articleDAO.getChapters();
                Platform.runLater(() -> root.setCenter(buildChapterGrid(chs)));
            });
        });

        VBox list = new VBox(12);
        list.setPadding(new Insets(24));

        Label heading = new Label("Chapter — " + chTitle);
        heading.getStyleClass().add("section-heading");
        list.getChildren().addAll(back, heading);

        for (Article a : articles) {
            list.getChildren().add(buildArticleRow(a));
        }

        ScrollPane scroll = new ScrollPane(list);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        return scroll;
    }

    private HBox buildArticleRow(Article a) {
        Label num     = new Label(String.valueOf(a.getArticleNumber()));
        num.getStyleClass().add("article-num");

        Label title   = new Label("Article " + a.getArticleNumber() + " — " + a.getTitle());
        title.getStyleClass().add("article-row-title");
        title.setWrapText(true);

        Button read   = new Button("Read");
        read.getStyleClass().add("read-btn");
        read.setOnAction(e -> nav.showArticle(a));

        HBox row = new HBox(16, num, title, read);
        row.getStyleClass().add("article-row");
        HBox.setHgrow(title, Priority.ALWAYS);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    public Node getRoot() { return root; }
}
