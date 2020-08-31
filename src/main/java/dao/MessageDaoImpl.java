package dao;

import database.PostgreSQLJDBC;
import models.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    @Override
    public List<Message> getAll() {
        final String SELECT_SQL = "SELECT * FROM messages;";
        try {
            PostgreSQLJDBC database = new PostgreSQLJDBC();
            Statement st = database.getConnection().createStatement();
            ResultSet rs = st.executeQuery(SELECT_SQL);
            List<Message> messageList = new ArrayList<>();
            while (rs.next()) {
                messageList.add(new Message()
                        .setId(rs.getInt("id"))
                        .setName(rs.getString("name"))
                        .setMessage(rs.getString("message")));
            }
            database.disconnect();
            return messageList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void add(Message message) {
        final String INSERT_SQL = "INSERT INTO messages (name, message)" +
                "VALUES (?, ?);";
        try {
            PostgreSQLJDBC database = new PostgreSQLJDBC();
            PreparedStatement ps = database.getConnection().prepareStatement(INSERT_SQL);
            ps.setString(1, message.getName());
            ps.setString(2, message.getMessage());
            ps.executeUpdate();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
