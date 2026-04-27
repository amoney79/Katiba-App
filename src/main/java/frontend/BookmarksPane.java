package frontend;

import backend.BookmarkDAO;
import backend.models.Bookmark;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookmarksPane {

    private final BorderPane root;
    private final MainWindow  nav;
    private final BookmarkDAO bookmarkDAO;
    private final VBox        listBox;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public BookmarksPane(MainWindow nav, BookmarkDAO bookmarkDAO) {
        this.nav         = nav;
        this.bookmarkDAO = bookmarkDAO;

        Label title = new Label("My Bookmarks");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Articles you've saved for quick access");
        subtitle.getStyleClass().add("page-subtitle");

        VBox header = new VBox(4, title, subtitle);
        header.getStyleClass().add("page-header");

        listBox = new VBox(14);
        listBox.setPadding(new Insets(24));

        ScrollPane scroll = new ScrollPane(listBox);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");

        root = new BorderPane();
        root.getStyleClass().add("content-pane");
        root.setTop(header);
        root.setCenter(scroll);
    }

    public void refresh() {
        listBox.getChildren().clear();
        List<Bookmark> bookmarks = bookmarkDAO.findAll();

        if (bookmarks.isEmpty()) {
            Label empty = new Label("No bookmarks yet.\nOpen an article and click ★ Bookmark.");
            empty.getStyleClass().add("empty-label");
            empty.setWrapText(true);
            listBox.getChildren().add(new StackPane(empty));
            return;
        }

        for (Bookmark bm : bookmarks) {
            listBox.getChildren().add(buildBookmarkCard(bm));
        }
    }

    private VBox buildBookmarkCard(Bookmark bm) {
        Label chapter = new Label("Chapter " + bm.getChapterNumber() + " — " + bm.getChapterTitle());
        chapter.getStyleClass().add("card-chapter-num");

        Label artTitle = new Label("Article " + bm.getArticleNumber() + ": " + bm.getArticleTitle());
        artTitle.getStyleClass().add("result-title");

        String dateStr = bm.getSavedAt() != null ? "Saved " + bm.getSavedAt().format(FMT) : "";
        Label date = new Label(dateStr);
        date.getStyleClass().add("result-snippet");

        // Note field
        TextField noteField = new TextField(bm.getNote() != null ? bm.getNote() : "");
        noteField.setPromptText("Add a personal note…");
        noteField.getStyleClass().add("search-field");

        Button saveNote = new Button("Save Note");
        saveNote.getStyleClass().add("secondary-btn");
        saveNote.setOnAction(e -> {
            bookmarkDAO.updateNote(bm.getId(), noteField.getText().trim());
            saveNote.setText("Saved ✓");
        });

        HBox noteRow = new HBox(8, noteField, saveNote);
        noteRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(noteField, Priority.ALWAYS);

        // Action buttons
        Button remove = new Button("✕ Remove");
        remove.getStyleClass().add("danger-btn");
        remove.setOnAction(e -> {
            bookmarkDAO.delete(bm.getId());
            refresh();
        });

        HBox actions = new HBox(8, remove);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(8, chapter, artTitle, date, noteRow, actions);
        card.getStyleClass().add("result-card");

        // Click to open article (we only have IDs; re-fetch via DAO would be ideal)
        artTitle.setStyle("-fx-cursor: hand;");

        return card;
    }

    public Node getRoot() { return root; }
}
