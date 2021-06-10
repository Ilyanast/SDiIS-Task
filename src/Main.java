import actors.Admin;
import actors.Secretary;
import actors.Writer;
import actors.impl.ConcreteAdmin;
import actors.impl.ConcreteSecretary;
import actors.impl.ConcreteWriter;
import storage.InmemoryStorage;
import storage.Storage;

public class Main {
    public static void main(String[] args) {
        Storage storage = new InmemoryStorage();

        Admin admin = new ConcreteAdmin(storage);
        admin.createCatalog("root/catalog");

        Writer writer = new ConcreteWriter(storage, "Ilya");
        writer.createDocument("root/myCoolDoc.txt", "This is my cool doc!");
        writer.createDocument("root/catalog/InnerDoc.txt", "This is my second cool doc!");

        Secretary secretary = new ConcreteSecretary(storage, "Dima");
        System.out.println(secretary.findByAuthorName("Ilya"));
    }
}
