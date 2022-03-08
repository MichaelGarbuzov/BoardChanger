package com.example.boardchanger.model.posts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BoardDao {

    @Query("select * from Board")
    List<Board> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Board... boards);

    @Query("DELETE FROM Board WHERE boardID = :boardId")
    void deleteBoardById(String boardId);

}
