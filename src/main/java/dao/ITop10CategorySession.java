package dao;

import domain.Top10CategorySession;

import java.util.List;

public interface ITop10CategorySession {
    void insertBatch(List<Top10CategorySession> data);
    void insert(Top10CategorySession data);
}
