package frontend;

import backend.ArticleDAO;
import backend.models.Article;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchPane {

    private final VBox root;
    private final MainWindow nav;
    private final ArticleDAO articleDAO;
    private final VBox resultsBox;
    private final Label resultCount;
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "katiba-search");
        t.setDaemon(true);
        return t;
    });

    public SearchPane(MainWindow nav, ArticleDAO articleDAO) {
        this.nav        = nav;
        this.articleDAO = articleDAO;

        // Header
        Label title = new Label("Search the Constitution");
        title.getStyleClass().add("page-title");

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search articles, rights, chapters…");
        searchField.getStyleClass().add("search-field");
        searchField.setMaxWidth(600);

        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("primary-btn");

        HBox searchBar = new HBox(12, searchField, searchBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 8, 0));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        resultCount = new Label("");
        resultCount.getStyleClass().add("result-count");

        VBox header = new VBox(8, title, searchBar, resultCount);
        header.getStyleClass().add("page-header");

        // Results
        resultsBox = new VBox(12);
        resultsBox.setPadding(new Insets(8, 24, 24, 24));

        ScrollPane scroll = new ScrollPane(resultsBox);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");

        root = new VBox(header, scroll);
        root.getStyleClass().add("content-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Wire actions
        Runnable doSearch = () -> {
            String q = searchField.getText().trim();
            if (!q.isBlank()) runSearch(q);
        };
        searchBtn.setOnAction(e -> doSearch.run());
        searchField.setOnAction(e -> doSearch.run());
    }

    private void runSearch(String query) {
        resultCount.setText("Searching…");
        resultsBox.getChildren().clear();

        executor.submit(() -> {
            List<Article> results = articleDAO.search(query);
            Platform.runLater(() -> {
                if (results.isEmpty()) {
                    resultCount.setText("No results for "" + query + """);
                    Label empty = new Label("Try different keywords.");
                    empty.getStyleClass().add("empty-label");
                    resultsBox.getChildren().add(empty);
                } else {
                    resultCount.setText(results.size() + " result(s) for "" + query + """);
                    for (Article a : results) {
                        resultsBox.getChildren().add(buildResultCard(a));
                    }
                }
            });
        });
    }

    private VBox buildResultCard(Article a) {
        Label chapter = new Label("Chapter " + a.getChapterNumber() + " — " + a.getChapterTitle());
        chapter.getStyleClass().add("card-chapter-num");

        Label artTitle = new Label("Article " + a.getArticleNumber() + ": " + a.getTitle());
        artTitle.getStyleClass().add("result-title");

        String preview = a.getContent() != null && a.getContent().length() > 160
                ? a.getContent().substring(0, 160) + "…"
                : (a.getContent() != null ? a.getContent() : "");
        Label snippet = new Label(preview);
        snippet.getStyleClass().add("result-snippet");
        snippet.setWrapText(true);

        Button read = new Button("Read Article →");
        read.getStyleClass().add("read-btn");
        read.setOnAction(e -> nav.showArticle(a));

        VBox card = new VBox(6, chapter, artTitle, snippet, read);
        card.getStyleClass().add("result-card");
        return card;
    }

    public Node getRoot() { return root; }
}
