package mypackage;



import javax.xml.crypto.Data;
import java.lang.module.FindException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {


    public static final String DB_NAME="jChatData.db";
    public static final String CONNECTION_STRING="jdbc:sqlite:C:\\Users\\redwa\\Desktop\\ChatServer\\"+DB_NAME;

    public static final String TABLE_LOGIN_INFO="loginInfo";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD="password";
    public static final int INDEX_COLUMN_USERNAME=1;
    public static final int INDEX_COLUMN_PASSWORD=2;
    public static ArrayList<User> users;
    private Connection connection;



    public static String GET_USER_PASSWORD="SELECT * FROM " + TABLE_LOGIN_INFO +  " WHERE "+ COLUMN_USERNAME + " = ?";
    public static String FIND_USER="SELECT COUNT(*) FROM " + TABLE_LOGIN_INFO + " WHERE " +COLUMN_USERNAME +" = ?";
    public static String INSERT_NEW_RECORD ="INSERT INTO " + TABLE_LOGIN_INFO + " VALUES( ? , ? )";

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
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(GET_USER_PASSWORD);
            preparedStatement.setString(1,username);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.isClosed()) return false;
            String str=resultSet.getString(INDEX_COLUMN_PASSWORD);
            resultSet.close();
            preparedStatement.close();
            if(str.equals(password))
                return true;
            else return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }

    public boolean doesUserExists(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            int n = resultSet.getInt(1);
            resultSet.close();
            preparedStatement.close();
            if(n>0){
                System.out.println("found user");
                return true;
            }
            else{
                System.out.println("not found");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void addNewUser(String username,String password)  {
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(INSERT_NEW_RECORD);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("added "+username +" " +password);
            connection.close();
            open();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNewContactTable(String username) throws SQLException {
//        StringBuilder sb=new StringBuilder("CREATE TABLE ");
//        String str=username+"ContactTable";
//        sb.append(str);
//        sb.append(" (friendUserName TEXT)");
//        Statement statement=connection.createStatement();
//        statement.executeUpdate(sb.toString());
//        statement.close();

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
            statement.close();
            resultSet.close();
            return messages;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void addMessage(String sender,String receiver,String chat) throws SQLException {
        System.out.println(chat);
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
        statement.close();
        System.out.println(chat+" added");
    }
}
