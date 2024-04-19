package interfaces;

import java.sql.Connection;

public interface Command {
    void execute(Connection conn);
}

class SystemInterfaceCommand implements Command {
    @Override
    public void execute(Connection conn) {
        SystemInterface.displaySystemInterface(conn);
    }
}

class CustomerInterfaceCommand implements Command {
    @Override
    public void execute(Connection conn) {
        CustomerInterface.displayCustomerInterface(conn);
    }
}

class BookstoreInterfaceCommand implements Command {
    @Override
    public void execute(Connection conn) {
        BookstoreInterface.displayBookstoreInterface(conn);
    }



}