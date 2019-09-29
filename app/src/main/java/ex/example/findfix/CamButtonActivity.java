package ex.example.findfix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.List;

public class CamButtonActivity extends FragmentActivity implements View.OnClickListener {
private Spinner mCategory_categories_spiner;
String url;
Button add,take;
    Button mButton;
    ImageView mImageView;
    Bitmap mBitmap;
    private int CAMERA_REQUEST = 4343;
    private static int TAKE_PICTURE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cam_button);
        add=findViewById(R.id.add);
        add.setEnabled(false);
        take=findViewById(R.id.take);
        add.setOnClickListener(this);
        take.setOnClickListener(this);
        mCategory_categories_spiner= (Spinner)findViewById(R.id.category_spinner);
        url="url";
        boolean checkRules=isStoragePermissionGranted();
        mImageView = (ImageView) findViewById(R.id.photo);



    }

    @Override
    public void onClick(View v) {
        if (v.getId()==findViewById(R.id.take).getId()){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
                String _path = Environment.getExternalStorageDirectory()
                        + File.separator + "T.jpg";
                File file = new File(_path);

                String templating=file.getAbsolutePath();
                File file2= new File(templating);

                Uri outputFileUri = Uri.fromFile(file2);
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, CAMERA_REQUEST);

             }
        else{
                Photo photo = new Photo();
                photo.setUrl(url);
                photo.setCategory(mCategory_categories_spiner.getSelectedItem().toString());
                new FirebaseDatabaseHelper().addPhoto(photo, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Photo> photos, List<String> keys) {
                        Toast.makeText(CamButtonActivity.this, "Recorded", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void DataInserted() {
                    }
                    @Override
                    public void DataUpdated() {
                    }
                    @Override
                    public void DataIsDeleted() {
                    }
                }); mImageView.setImageDrawable(null); add.setEnabled(false);

        } }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        add.setEnabled(true);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            String _path = Environment.getExternalStorageDirectory() + File.separator + "T.jpg";
            mBitmap = BitmapFactory.decodeFile(_path);
            mImageView.animate().rotation(getExifAngle(_path));
            mImageView.setScaleType(ImageView.ScaleType.FIT_START);
            mImageView.setImageBitmap(mBitmap);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public static int getExifAngle(String path) {
        int angle = 0;
        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
        } catch (Exception e) {
        }
        return angle;
    }
}