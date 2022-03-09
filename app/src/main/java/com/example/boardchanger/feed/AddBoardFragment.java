package com.example.boardchanger.feed;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.boardchanger.R;
import com.example.boardchanger.model.posts.Board;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.example.boardchanger.shared.ImageHandler;
import com.squareup.picasso.Picasso;

import java.io.InputStream;


public class AddBoardFragment extends Fragment {

    EditText boardName;
    EditText boardPrice;
    EditText boardDesc;
    EditText boardAddress;
    EditText boardYear;
    EditText boardPhoneNum;
    Button addBoard, deleteBoard;
    ProgressBar progressBar;
    String boardID;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_SELECTION = 2;
    Bitmap imageBitmap;
    ImageView boardP;
    ImageButton galleryBtn;
    ImageButton camBtn;
    String imageCat = "/board_pictures/";
    Boolean isEditMode = false;
    Board currentBoard;

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
        boardP = view.findViewById(R.id.add_board_image_preview);
        boardPhoneNum = view.findViewById(R.id.add_board_phone_num);
        deleteBoard = view.findViewById(R.id.edit_board_delete_btn);
        deleteBoard.setVisibility(View.GONE);
        isEditMode = AddBoardFragmentArgs.fromBundle(getArguments()).getIsEditMode();
        boardID = AddBoardFragmentArgs.fromBundle(getArguments()).getBoardID();

        addBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    editBoard();
                } else {
                    add();
                }
            }
        });

        camBtn = view.findViewById(R.id.add_board_take_image_btn);
        galleryBtn = view.findViewById(R.id.add_board_add_image_btn);
        if (boardID != null) {
            showProgressBar(true);
            Model.instance.getBoardByID(boardID, new Model.getBoardByID() {
                @Override
                public void onComplete(Board board) {
                    showProgressBar(false);
                    currentBoard = board;
                    boardName.setText(board.getName());
                    boardDesc.setText(board.getDescription());
                    boardPrice.setText(board.getPrice());
                    boardAddress.setText(board.getAddress());
                    boardYear.setText(board.getYear());
                    boardPhoneNum.setText(Board.getPhoneNum());
                    Picasso.get().load(board.getImageUrl()).into(boardP);
                }
            });
        }
        camBtn.setOnClickListener(v -> {
            Intent intent = ImageHandler.openCamera();
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        });

        galleryBtn.setOnClickListener(v -> {
            Intent intent = ImageHandler.openGallery();
            startActivityForResult(intent, REQUEST_IMAGE_SELECTION);
        });

        Fragment thisFrag = this;
        deleteBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                Model.instance.deleteBoard(currentBoard, new Model.CompleteListener() {
                    @Override
                    public void onComplete() {
                        thisFrag.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Model.instance.refreshBoardsList();
                                showProgressBar(false);
                                NavHostFragment.findNavController(thisFrag).navigateUp();
                            }
                        });
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                boardP.setImageBitmap(imageBitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_SELECTION) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    boardP.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed to select image", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private Boolean validate() {
        Boolean valid = true;
        if (boardYear.getText().toString().equals("") || boardDesc.getText().toString().equals("") ||
                boardName.getText().toString().equals("") || boardPrice.getText().toString().equals("") ||
                boardAddress.getText().toString().equals("") || boardPhoneNum.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "All Fields Must be Filled!", Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }

    private void showProgressBar(Boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE: View.GONE);
        addBoard.setEnabled(!show);
        camBtn.setEnabled(!show);
        galleryBtn.setEnabled(!show);
        deleteBoard.setEnabled(!show);
    }

    private Board createBoardFromDetails() {

        String name = boardName.getText().toString();
        String price = boardPrice.getText().toString();
        String desc = boardDesc.getText().toString();
        String year = boardYear.getText().toString();
        String phoneNum = boardPhoneNum.getText().toString();
        String address = boardAddress.getText().toString();

        if(isEditMode && currentBoard != null) {
            currentBoard.setName(name);
            currentBoard.setPrice(price);
            currentBoard.setDescription(desc);
            currentBoard.setYear(year);
            currentBoard.setPhoneNum(phoneNum);
            currentBoard.setAddress(address);
            return currentBoard;
        } else {
            Board board = new Board(name, year, price, desc, address);
            board.setPhoneNum(phoneNum);
            board.setUser(User.getInstance().getEmail());
            return board;
        }
    }

    private void add() {
        if(validate()) {
            showProgressBar(true);
            Board newBoard = createBoardFromDetails();

            newBoard.setUser(User.getInstance().getEmail());
            Model.instance.saveImage(imageBitmap, newBoard.getName() + ".jpg", imageCat, url -> {
                newBoard.setImageUrl(url);
                Model.instance.addBoard(newBoard, () -> {
                    Model.instance.refreshBoardsList();
                    Navigation.findNavController(boardName).navigateUp();
                });
            });
        }
    }

    private void editBoard() {
        if(validate()){
            showProgressBar(true);
            Board board = createBoardFromDetails();
            Fragment thisFragment = this;
            Model.instance.editBoard(board, new Model.CompleteListener() {
                @Override
                public void onComplete() {
                    Toast.makeText(getActivity(), "Board Updated!", Toast.LENGTH_LONG).show();
                    Model.instance.refreshBoardsList();
                    NavHostFragment.findNavController(thisFragment).popBackStack();
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isEditMode) {
            addBoard.setText("Edit Board");
            MainFeedActivity activity = (MainFeedActivity) getActivity();
            activity.toolbar.setTitle(R.string.edit_board_title);
            deleteBoard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isEditMode) {
            menu.findItem(R.id.menuProfileFragment).setVisible(false);
        }
    }
}