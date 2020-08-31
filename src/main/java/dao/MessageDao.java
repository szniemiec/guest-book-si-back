package dao;

import models.Message;

import java.util.List;

public interface MessageDao {

    List<Message> getAll();

    void add(Message message);

}
