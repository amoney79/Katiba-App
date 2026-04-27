# Katiba App 🇰🇪
**Kenya Constitution 2010 — JavaFX Desktop Reader**

A modern desktop application for reading, searching and bookmarking the Constitution of Kenya (2010), built with JavaFX and MongoDB Atlas.

---

## Features
| Feature | Description |
|---|---|
| 📖 Browse | All 18 chapters and 260+ articles |
| 🔍 Search | Full-text search powered by MongoDB Atlas text index |
| 🔖 Bookmarks | Save articles with personal notes, persisted to Atlas |
| 🌙 Dark theme | Kenyan-inspired green & crimson on near-black |

---

## Project Structure
```
Katiba-App/
├── pom.xml                          ← Maven build config
└── src/main/
    ├── java/
    │   ├── module-info.java
    │   ├── app/
    │   │   ├── KatibaApp.java        ← Entry point (extends Application)
    │   │   └── DataSeeder.java       ← One-time DB seed script
    │   ├── backend/
    │   │   ├── DatabaseManager.java  ← MongoDB Atlas singleton
    │   │   ├── ArticleDAO.java       ← Article CRUD
    │   │   ├── BookmarkDAO.java      ← Bookmark CRUD
    │   │   └── models/
    │   │       ├── Article.java
    │   │       └── Bookmark.java
    │   └── frontend/
    │       ├── MainWindow.java       ← Root layout + sidebar nav
    │       ├── HomePane.java         ← Chapter grid + article list
    │       ├── SearchPane.java       ← Full-text search UI
    │       ├── BookmarksPane.java    ← Saved bookmarks UI
    │       └── ArticleDetailPane.java ← Article reader
    └── resources/
        ├── app/config.properties     ← MongoDB URI (fill this in!)
        └── styles/main.css           ← Dark theme CSS
```

---

## Setup

### 1. Prerequisites
- **JDK 21+** — [Download](https://adoptium.net/)
- **Maven 3.9+** — [Download](https://maven.apache.org/)
- **MongoDB Atlas** account — [Free tier](https://www.mongodb.com/cloud/atlas)

### 2. Configure MongoDB Atlas

1. Create a free **M0 cluster** on Atlas
2. Create a database user with read/write access
3. Whitelist your IP address (or use `0.0.0.0/0` for dev)
4. Copy your **connection string** from: *Connect → Drivers → Java*
5. Paste it into `src/main/resources/app/config.properties`:

```properties
mongodb.uri=mongodb+srv://<user>:<password>@<cluster>.mongodb.net/?retryWrites=true&w=majority
mongodb.database=katiba
```

### 3. Seed the Database (run once)
```powershell
mvn exec:java -Dexec.mainClass="app.DataSeeder"
```
This inserts ~50 key articles across all 18 chapters.

### 4. Run the App
```powershell
mvn javafx:run
```

---

## Tech Stack
| Layer | Technology |
|---|---|
| UI | JavaFX 21 |
| Database | MongoDB Atlas (mongodb-driver-sync 5.0) |
| Build | Maven 3.9 |
| Language | Java 21 |

---

## MongoDB Collections

### `articles`
```json
{
  "_id": ObjectId,
  "chapterNumber": 4,
  "chapterTitle":  "The Bill of Rights",
  "articleNumber": 33,
  "title":         "Freedom of Expression",
  "content":       "Every person has the right to freedom of expression...",
  "tags":          ["freedom", "expression", "speech"]
}
```

### `bookmarks`
```json
{
  "_id": ObjectId,
  "articleId":     ObjectId,
  "articleNumber": 33,
  "articleTitle":  "Freedom of Expression",
  "chapterNumber": 4,
  "chapterTitle":  "The Bill of Rights",
  "note":          "Important for media law",
  "savedAt":       ISODate
}
```

---

*Constitution of Kenya, 2010 — Published by the National Council for Law Reporting*
