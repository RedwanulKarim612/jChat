package mypackage;



import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private static DataSource instance= new DataSource();

    public static final String DB_NAME="jChatData.db";
    public static final String CONNECTION_STRING="jdbc:sqlite:\\C:\\Users\\redwa\\Desktop\\ChatServer\\"+DB_NAME;

    public static final String TABLE_LOGIN_INFO="loginInfo";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD="password";
    public static final int INDEX_COLUMN_USERNAME=1;
    public static final int INDEX_COLUMN_PASSWORD=2;
    public static ArrayList<User> users;
    private Connection connection;

    public static DataSource getInstance(){
        return instance;
    }

    public boolean open(){
        try {
            connection= DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("CANNOT OPEN CONNECTION "+e.getMessage());
            return false;
        }
    }

    public void close(){
        try {
            if(connection!=null) connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isLoginValid(String username,String password)  {
        StringBuilder sb=new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_LOGIN_INFO);
        try(
        Statement statement=connection.createStatement();

        ResultSet resultSet=statement.executeQuery(sb.toString())) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserName(resultSet.getString(INDEX_COLUMN_USERNAME));
                user.setPassword(resultSet.getString(INDEX_COLUMN_PASSWORD));
                if(user.getUserName().equals(username) && user.getPassword().equals(password)){
                    return true;
                }
            }
            return false;
        }
        catch (SQLException e){
            return false;
        }
    }

    public boolean doesUserExists(String username){
        StringBuilder sb=new StringBuilder("SELECT username FROM ");
        sb.append(TABLE_LOGIN_INFO);
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sb.toString());
            while(resultSet.next()){
                String str=resultSet.getString(INDEX_COLUMN_USERNAME);
                if(str.equals(username)) {
                    System.out.println("found user");
                    return true;
                }
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void addNewUser(String username,String password)  {
        StringBuilder sb=new StringBuilder("INSERT INTO loginInfo VALUES('");
        sb.append(username);
        sb.append("','");
        sb.append(password);
        sb.append("')");
        try {
            Statement statement = connection.createStatement();

            statement.execute(sb.toString());
//            System.out.println(sb.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNewContactTable(String username) throws SQLException {
        StringBuilder sb=new StringBuilder("CREATE TABLE ");
        String str=username+"ContactTable";
        sb.append(str);
        sb.append(" (friendUserName TEXT)");
        Statement statement=connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    public void createMessageTable(String username) throws SQLException {
        StringBuilder sb=new StringBuilder("CREATE TABLE ");
        sb.append(username);
        sb.append("MessageTable (friendUserName TEXT,sender TEXT,chat TEXT,time TEXT)");
        Statement statement=connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    public List<Message> getAllMessages(String username){
        StringBuilder sb=new StringBuilder("SELECT friendUsername,sender,chat FROM ");
        sb.append(username);
        sb.append("MessageTable");
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sb.toString());
            List<Message>messages=new ArrayList<>() ;
            while(resultSet.next()){
                String friend=resultSet.getString(1);
                String sender=resultSet.getString(2);
                String chat=resultSet.getString(3);

                Message message=new Message();
                message.setFriendUserName(friend);
                message.setSender(sender);
                message.setChat(chat);

                messages.add(message);
//                String friend=resultSet.getString(1);

            }
            return messages;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void addMessage(String sender,String receiver,String chat) throws SQLException {
        StringBuilder sb=new StringBuilder("INSERT INTO ");
        sb.append(sender);
        sb.append("MessageTable (friendUserName,sender,chat) VALUES('");
        sb.append(receiver);
        sb.append("','");
        sb.append(sender);
        sb.append("','");
        sb.append(chat);
        sb.append("')");
        Statement statement=connection.createStatement();
        statement.execute(sb.toString());
        sb=new StringBuilder("INSERT INTO ");
        sb.append(receiver);
        sb.append("MessageTable (friendUserName,sender,chat) VALUES('");
        sb.append(sender);
        sb.append("','");
        sb.append(sender);
        sb.append("','");
        sb.append(chat);
        sb.append("')");

        statement=connection.createStatement();
        statement.execute(sb.toString());
    }
}
