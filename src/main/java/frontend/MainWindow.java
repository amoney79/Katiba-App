package frontend;

import backend.ArticleDAO;
import backend.BookmarkDAO;
import backend.models.Article;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainWindow {

    private final BorderPane root;
    private final ArticleDAO  articleDAO  = new ArticleDAO();
    private final BookmarkDAO bookmarkDAO = new BookmarkDAO();

    // Panels
    private final HomePane        homePane;
    private final SearchPane      searchPane;
    private final BookmarksPane   bookmarksPane;
    private final ArticleDetailPane detailPane;

    // Sidebar buttons
    private Button activeNav;

    public MainWindow(Stage stage) {
        detailPane    = new ArticleDetailPane(this, bookmarkDAO);
        homePane      = new HomePane(this, articleDAO);
        searchPane    = new SearchPane(this, articleDAO);
        bookmarksPane = new BookmarksPane(this, bookmarkDAO);

        root = new BorderPane();
        root.setLeft(buildSidebar());
        showHome();
    }

    // ─── Navigation ───────────────────────────────────────────────────────────

    public void showHome()      { setContent(homePane.getRoot());      setActive(null); }
    public void showSearch()    { setContent(searchPane.getRoot());    }
    public void showBookmarks() { setContent(bookmarksPane.getRoot()); bookmarksPane.refresh(); }

    public void showArticle(Article article) {
        detailPane.load(article);
        setContent(detailPane.getRoot());
    }

    private void setContent(Node node) {
        root.setCenter(node);
    }

    // ─── Sidebar ──────────────────────────────────────────────────────────────

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(220);

        // Logo
        Label logo = new Label("KATIBA");
        logo.getStyleClass().add("sidebar-logo");
        Label sub = new Label("Kenya Constitution");
        sub.getStyleClass().add("sidebar-sub");

        VBox logoBox = new VBox(4, logo, sub);
        logoBox.getStyleClass().add("sidebar-logo-box");

        // Nav items
        Button btnHome      = navButton("🏠  Home",      () -> showHome());
        Button btnSearch    = navButton("🔍  Search",    () -> showSearch());
        Button btnBookmarks = navButton("🔖  Bookmarks", () -> showBookmarks());

        activeNav = btnHome;
        btnHome.getStyleClass().add("nav-active");

        // Store refs for active toggling
        btnHome.setOnAction(e      -> { activateNav(btnHome);      showHome(); });
        btnSearch.setOnAction(e    -> { activateNav(btnSearch);    showSearch(); });
        btnBookmarks.setOnAction(e -> { activateNav(btnBookmarks); showBookmarks(); });

        VBox nav = new VBox(6, btnHome, btnSearch, btnBookmarks);
        nav.setPadding(new Insets(16, 12, 0, 12));

        // Version footer
        Label version = new Label("v1.0 — CoK 2010");
        version.getStyleClass().add("sidebar-footer");

        VBox.setVgrow(nav, Priority.ALWAYS);
        sidebar.getChildren().addAll(logoBox, nav, version);
        return sidebar;
    }

    private Button navButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void activateNav(Button btn) {
        if (activeNav != null) activeNav.getStyleClass().remove("nav-active");
        btn.getStyleClass().add("nav-active");
        activeNav = btn;
    }

    private void setActive(Button btn) {}

    public Node getRoot() { return root; }
}
