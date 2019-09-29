package ex.example.findfix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class PhotoListActivity extends AppCompatActivity {
private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        mRecyclerView =(RecyclerView)findViewById(R.id.recyclerview_photos);
        new FirebaseDatabaseHelper().readPhotos(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Photo> photos, List<String> keys) {
                new RecyclerView_Config().setConfig(mRecyclerView,PhotoListActivity.this,photos,keys);
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
        });
    }
}
