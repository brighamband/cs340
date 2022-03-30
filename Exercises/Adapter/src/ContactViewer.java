public class ContactViewer implements TableData {
    ContactManager contactManager;

    public ContactViewer(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    @Override
    public int getColumnCount() {
        return 4;   // 4 attributes
    }

    @Override
    public int getRowCount() {
        return contactManager.getContactCount();
    }

    @Override
    public int getColumnSpacing() {
        return 2;
    }

    @Override
    public int getRowSpacing() {
        return 2;
    }

    @Override
    public char getHeaderUnderline() {
        return '-';
    }

    @Override
    public String getColumnHeader(int col) {
        if (col == 0) {
            return "First Name";
        } else if (col == 1) {
            return "Last Name";
        } else if (col == 2) {
            return "Phone";
        } else if (col == 3) {
            return "Email";
        }
        return null;
    }

    @Override
    public int getColumnWidth(int col) {
        return 20;
    }

    @Override
    public Justification getColumnJustification(int col) {
        return Justification.Center;
    }

    @Override
    public String getCellValue(int row, int col) {
        Contact contact = contactManager.getContact(row);
        if (col == 0) {
            return contact.getFirstName();
        } else if (col == 1) {
            return contact.getLastName();
        } else if (col == 2) {
            return contact.getPhone();
        } else if (col == 3) {
            return contact.getEmail();
        }
        return null;
    }
}
