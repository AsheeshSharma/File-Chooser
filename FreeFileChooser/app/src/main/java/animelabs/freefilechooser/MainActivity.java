package animelabs.freefilechooser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import animelabs.library.FileChooser;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_IMAGE_FILE = 1;
    public String mSavedProfileImage;
    public FileChooser fileChooser;
    public ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fileChooser=new FileChooser(MainActivity.this);
        imageView=(ImageView)findViewById(R.id.imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items_without_grouppic = {"Choose from Library", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items_without_grouppic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items_without_grouppic[item].equals("Choose from Library")) {
                            //instead of sending the result directly, we send the user to AskToSend activity
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            photoPickerIntent.setType("image/jpg");
                            photoPickerIntent.putExtra("crop", "true");

                            photoPickerIntent.putExtra("outputX", 300);
                            photoPickerIntent.putExtra("outputY", 300);
                            photoPickerIntent.putExtra("aspectX", 1);
                            photoPickerIntent.putExtra("aspectY", 1);

                            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
                            photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                            startActivityForResult(photoPickerIntent, REQUEST_IMAGE_FILE);
                        } else if (items_without_grouppic[item].equals("Cancel")) {
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }
    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Eckovation");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Failed", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        mSavedProfileImage = mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg";


        return mediaFile;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_FILE && resultCode == RESULT_OK) {


            Uri selectedImageUri = data.getData();
            int currentapiVersion = fileChooser.getVersion();
            mSavedProfileImage = fileChooser.getPath(MainActivity.this, selectedImageUri,currentapiVersion);
            File file=new File(mSavedProfileImage);
            if(file.exists()){
                Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
