package interfaces;

public interface Command {
    void execute();
    public class SystemInterfaceCommand implements Command {
        @Override
        public void execute() {
            SystemInterface.displaySystemInterface();
        }
    }
    
    public class CustomerInterfaceCommand implements Command {
        @Override
        public void execute() {
            CustomerInterface.displayCustomerInterface();
        }
    }
    
    public class BookstoreInterfaceCommand implements Command {
        @Override
        public void execute() {
            BookstoreInterface.displayBookstoreInterface();
        }
    }

    
}
