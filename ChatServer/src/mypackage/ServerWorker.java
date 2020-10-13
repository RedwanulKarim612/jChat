package mypackage;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread{
    private final Socket clientSocket;
    private final Server server;
    private String login;
    private OutputStream outputStream;
    private HashSet<String> topicSet=new HashSet<>();
    private DataSource dataSource=new DataSource();

    public ServerWorker(Server server, Socket clientSocket) {
        this.server=server;
        this.clientSocket = clientSocket;
        dataSource.open();
    }

    public String getLogin(){
        return login;
    }
    @Override
    public void run() {
        try {
            handleClientSocket(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket(Socket clientSocket) throws IOException{
        InputStream inputStream=clientSocket.getInputStream();

        this.outputStream=clientSocket.getOutputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line = null;
        try {
            while ((line=reader.readLine())!=null) {
                String []tokens=line.split(" ");
                if(tokens!=null && tokens.length>0) {
                    System.out.println(line);
                    String cmd=tokens[0];
                    if(cmd.equals("quit") || cmd.equals("logoff")){
                        clientSocket.close();
                        handleLogoff();
                        break;
                    }
                    else if(cmd.equals("signup")){
                        handleSignup(tokens);
                    }
                    else if(cmd.equals("login") ){
                        handleLogin(outputStream,tokens);
                    }
                    else if(cmd.equals("message")){
                        String[] tokensMsg=line.split(" ",3);
                        handleMessage(tokensMsg);
                    }
                    else if(cmd.equals("join")){
                        handleJoin(tokens);
                    }
                    else if(cmd.equals("leave")){
                        handleLeave(tokens);
                    }
                    else if(cmd.equals("search")){
                        handleSearch(tokens);
                    }
                    else{
                        String msg="invalid command\n";
                        outputStream.write(msg.getBytes());
                    }
                }
            }
        }
        catch (Exception e){
//            exception dise to ki hoise???
        }
        clientSocket.close();
    }

    private void handleSignup(String[] tokens) throws IOException, SQLException {
        if(tokens.length==3){
            String username=tokens[1];
            String password=tokens[2];
            String msg=null;
            if(!dataSource.doesUserExists(username)) {
                msg="signup successful\n";
                this.login=username;
                dataSource.addNewUser(username,password);
                dataSource.createMessageTable(username);
            }
            else msg="signup unsuccessful\n";
            this.send(msg);
        }
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException, SQLException {
        if(tokens.length==3){
            String username=tokens[1];
            String password=tokens[2];
            String msg=null;
            if(dataSource.isLoginValid(username,password) ){
                msg="login successful\n";
                this.send(msg);
                this.login=username;
                sendInfoToUser(username);

                return;
            }
            else {
                msg="login unsuccessful\n";
                this.send(msg);
            }
        }
    }

    private void handleLeave(String[] tokens) {
        if(tokens.length>1){
            String topic=tokens[1];
            topicSet.remove(topic);
        }
    }

    public boolean isMemberOfTopic(String topic){
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) {
        if(tokens.length>1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    private void handleMessage(String[] tokens) throws IOException, SQLException {
        String sendTo=tokens[1];
        String body=tokens[2];

        boolean isTopic=sendTo.charAt(0)=='#';
        List<ServerWorker> workerList=server.getWorkerList();
        for(ServerWorker worker:workerList){
            if(isTopic){
                if(worker.isMemberOfTopic(sendTo)){
                    String outMsg="msg in "+sendTo+" from " + login+" : " +body+"\n";
                    worker.send(outMsg);
                }
            }
            else {
                if (sendTo.equals(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + login + " "+ body + "\n";
                    worker.send(outMsg);
                }
            }
        }
        dataSource.addMessage(login,sendTo,body);
    }



    private void handleLogoff() throws IOException {
        this.clientSocket.close();
        outputStream.close();
        List<ServerWorker>workerList=server.getWorkerList();
        server.removeWorker(this);
        for(ServerWorker worker : workerList)
//            System.out.println(worker.getLogin()+"\n");
            send("logoff\n");
        dataSource.close();
    }


    private void send(String msg) throws IOException {
        this.outputStream.write(msg.getBytes());
        System.out.println(msg);
    }

    private void handleSearch(String tokens[]) throws IOException {
        String msg=null;
        if(dataSource.doesUserExists(tokens[1])) msg="found\n";
        else msg="not found\n";
        this.send(msg);
    }

    private void sendInfoToUser(String username) throws IOException, SQLException {
        List<Message>messages = dataSource.getAllMessages(username);
        for(Message message:messages){
            String cmd="msg "+message.getFriendUserName()+" " +message.getSender()+" " +message.getChat()+"\n";
            String []tokens=cmd.split(" ",4);
            this.send(cmd);
        }
    }
}
