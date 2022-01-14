package com.example.boardchanger.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model(){
        for(int i=0;i<20;i++){
            Board b = new Board("180$"+i,"mic"+i,"1922"+i);
            data.add(b);
        }
    }
    List<Board> data = new LinkedList<Board>();

    public List<Board> getAllBoards() {
        return data;
    }

    public Board getBoardByName(String boardName){
        for (Board board:data){
            if(board.getName().equals(boardName)){
                return board;
            }
        }
        return null;
    }

    public void addBoard(Board board){
        data.add(board);
    }
}
