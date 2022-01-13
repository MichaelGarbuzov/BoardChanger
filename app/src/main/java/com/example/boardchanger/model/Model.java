package com.example.boardchanger.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){

        Board board = new Board();
        board.name = "Kali" ;
        board.price = "100$";
        board.year = "2018";
        data.add(board);
    }
    List<Board> data = new LinkedList<Board>();

    public List<Board> getAllBoards() {
        return data;
    }
}
