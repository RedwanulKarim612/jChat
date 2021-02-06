package com.example.jchatandroid;


import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;

public class ChatClient {
    public static ChatClient instance=new ChatClient("192.168.0.105",5003);

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
    public ClientListener clientListener;
    public String response;

    public ChatClient(String serverName, int serverPort) {
        this.serverName=serverName;
        this.serverPort=serverPort;
//        clientListener.start();
    }

    public static ChatClient getInstance(){
        return instance;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getServerIn() {
        return serverIn;
    }

    public BufferedReader getBufferedIn() {
        return bufferedIn;
    }

    private void logoff() throws IOException {
        String cmd= "logoff";
        serverOut.write(cmd.getBytes());

    }

    boolean signUp(String username,String password) throws IOException {
        String cmd="signup "+username+" " + password +"\n";
        serverOut.write(cmd.getBytes());
        while(true){
            response=clientListener.response;
            if(response!=null){
                if(response.equals("signup successful")){
                    response=null;
                    return true;
                }
                else if(response.equals("signup unsuccessful")){
                    response=null;
                    return false;
                }
            }
        }
    }
    boolean login(String username,String password) throws IOException {
        String cmd="login "+ username +" " +password+"\n";
        serverOut.write(cmd.getBytes());
//        String response=bufferedIn.readLine();
//        System.out.println("from server : "+response);
//        String response=Main.client.clientListener.getResponse();
        while(true){
            response=clientListener.response;
            if(response!=null)
                if(response.equals("login successful") || response.equals("login unsuccessful"))
                    break;
        }

        if(response.equals("login successful")){
            response=null;
            return true;
        }
        response=null;
        return false;
    }

    boolean searchUser(String friendUserName) throws IOException {
        String cmd="search "+friendUserName+"\n";
        serverOut.write(cmd.getBytes());

        while(true){
            response=clientListener.response;
            if(response!=null){
                if(response.equals("found") || response.equals("not found"))
                    break;

            }
        }
        if(response!=null)
            if(response.equals("found")) {
                response=null;
                return true;
            }
        response=null;
        return false;
    }

    void send(String userName,String message) throws IOException {
        String cmd="message "+ userName+" " +message+"\n";
        serverOut.write(cmd.getBytes());
    }
    boolean connect() {
        try {
            this.socket=new Socket(serverName,serverPort);
            this.serverOut=socket.getOutputStream();
            this.serverIn=socket.getInputStream();
            this.bufferedIn =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientListener=new ClientListener(bufferedIn);
            clientListener.start();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}