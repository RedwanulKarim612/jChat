package mypackage;

public class Main {

public static void main(String[] args) {
        int port=1234;
        Server server = new Server(port);
        server.start();
    }
}


