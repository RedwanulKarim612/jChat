package mypackage;

public class Main {

    public static void main(String[] args) {
                DataSource.getInstance().open();
                int port=5003;
                Server server = new Server(port);
                server.start();
            }
        }


