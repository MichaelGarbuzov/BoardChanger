package com.example.boardchanger.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.boardchanger.MyApplication;
import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.posts.BoardDao;
import com.example.boardchanger.model.users.User;
import com.example.boardchanger.model.users.UserDao;

@Database(entities = {Board.class, User.class}, version = 9)
abstract class BoardChangerDBRepository extends RoomDatabase{
    public abstract BoardDao boardDao();
}

public class BoardChangerLocalDB{
    static public BoardChangerDBRepository db =
            Room.databaseBuilder(MyApplication.getContext(), BoardChangerDBRepository.class,
                    "BoardChangerDB.db")
                    .fallbackToDestructiveMigration().build();

}
