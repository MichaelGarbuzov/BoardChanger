package com.example.boardchanger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.boardchanger.model.Board;
import com.example.boardchanger.model.Model;


public class AddBoardFragment extends Fragment {

    EditText boardName;
    EditText boardPrice;
    EditText boardDesc;
    EditText boardAddress;
    EditText boardYear;
    Button addBoard;
    ProgressBar progressBar;

    public AddBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_board, container, false);
        boardName = view.findViewById(R.id.add_board_model);
        boardPrice = view.findViewById(R.id.add_board_price);
        boardDesc = view.findViewById(R.id.add_board_desc);
        boardAddress = view.findViewById(R.id.add_board_address);
        boardYear = view.findViewById(R.id.add_board_year);
        addBoard = view.findViewById(R.id.add_board_add_board_btn);
        progressBar = view.findViewById(R.id.add_board_progressbar);
        progressBar.setVisibility(View.GONE);

        addBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        return view;
    }
    private void add(){
        progressBar.setVisibility(View.VISIBLE);
        addBoard.setEnabled(false);
        String name = boardName.getText().toString();
        String price = boardPrice.getText().toString();
        String desc = boardDesc.getText().toString();
        String year = boardYear.getText().toString();
        String address = boardAddress.getText().toString();
        Log.d("TAG","saved name:" + name + " id:" + boardName + " flag:" + boardPrice);
        Board board = new Board(name,year,price, desc, address);
        Model.instance.addBoard(board,()->{
            Navigation.findNavController(boardName).navigateUp();
        });
    }
}