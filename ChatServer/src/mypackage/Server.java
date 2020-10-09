package mypackage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class  Server extends Thread{
    private final int serverPort;
    private ArrayList<ServerWorker>workerList=new ArrayList<>();
    public Server(int port) {
        this.serverPort=port;
    }
    public List<ServerWorker> getWorkerList(){
        return workerList;
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(serverPort);
            while(true){
                Socket clientSocket=serverSocket.accept();
                System.out.println("client connected");
                ServerWorker worker=new ServerWorker(this,clientSocket);

                worker.start();
                workerList.add(worker);
            }
        } catch (IOException e) {
            System.out.println("connection failed");
            e.printStackTrace();
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
}
