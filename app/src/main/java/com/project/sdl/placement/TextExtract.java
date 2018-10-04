package com.project.sdl.placement;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;

public class TextExtract extends AppCompatActivity implements View.OnClickListener{

    private static final int SELECT_PICTURE = 100;
    ImageView i1;
    TextView t1;
    public Uri selectedImageUri;
    Button Btn;
    String path;
    File imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_extract);

    t1 =(TextView)findViewById(R.id.textext);
    Btn = (Button)findViewById(R.id.Extract);
    i1 = (ImageView)findViewById(R.id.img1);
    findViewById(R.id.btnSelectImage).setOnClickListener(this);


    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    path = getPathFromURI(selectedImageUri);

                    // Set the image in ImageView
                   ((ImageView) findViewById(R.id.img1)).setImageURI(selectedImageUri);


                }
            }
        }

    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void onClick(View v) {
        openImageChooser();
    }

    public void gettextfromImage(View v) {
        Resources r= this.getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(r,R.drawable.p1);
        Bitmap bitmap = ((BitmapDrawable)i1.getDrawable()).getBitmap();

        TextRecognizer textRecognizer = new  TextRecognizer.Builder(getApplicationContext()).build();

        if(!textRecognizer.isOperational())
        {
            Toast.makeText(getApplicationContext(), "Can not Extract", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for(int i=0 ; i<items.size();i++)
            {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }

            t1.setText(sb.toString());
        }
    }
}
