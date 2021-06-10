package actors;

public interface Writer {
    void createDocument(String fullPath, String content);
    void updateDocument(String fullPath, String newName, String newContent);
}
