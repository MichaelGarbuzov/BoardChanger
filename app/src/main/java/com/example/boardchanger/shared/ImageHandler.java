package com.example.boardchanger.shared;

import static android.app.Activity.RESULT_OK;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import static com.example.boardchanger.MyApplication.getContext;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class ImageHandler {

    public static Intent openGallery(){
        Intent openGalleryIntent = new Intent();
        openGalleryIntent.setType("image/*");
        openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        return openGalleryIntent;

    }

    public static Intent openCamera() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

}
