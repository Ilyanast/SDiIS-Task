package storage.structure;

import java.util.Date;

public class Document extends StorageUnit {
    private final Date creationDate;
    private String authorName;
    private String content;

    public Document(String name, String authorName, String content) {
        super(name);
        this.creationDate = new Date();
        this.authorName = authorName;
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
