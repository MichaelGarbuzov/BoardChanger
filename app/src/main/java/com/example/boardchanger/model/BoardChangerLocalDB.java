package com.example.boardchanger.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.boardchanger.MyApplication;

@Database(entities = {Board.class}, version =1)
abstract class BoardChangerDBRepository extends RoomDatabase{
    public abstract BoardDao boardDao();
}

public class BoardChangerLocalDB{
    static public BoardChangerDBRepository db =
            Room.databaseBuilder(MyApplication.getContext(), BoardChangerDBRepository.class, "BoardChangerDB.db")
            .fallbackToDestructiveMigration().build();

}
