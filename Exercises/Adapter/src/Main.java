import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Main {
    public static void main(String[] args) {
        ContactManager contactManager = new ContactManager();
        // Add new contacts
        for (int i = 0; i < 10; i++) {
            Contact newContact = new Contact(
                String.format("firstName%d", i),
                String.format("lastName%d", i),
                String.format("801-555-012%d", i),
                String.format("email%d@gmail.com" ,i)
            );
            contactManager.addContact(newContact);
        }

        // Set up table parameters
        Writer output = new PrintWriter(System.out);
        ContactViewer contactViewer = new ContactViewer(contactManager);

        Table table = new Table(output, contactViewer);
        try {
            table.display();
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
