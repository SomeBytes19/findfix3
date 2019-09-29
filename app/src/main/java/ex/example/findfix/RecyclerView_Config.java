package ex.example.findfix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
 private Context mContext;
private PhotosAdapter mPhotosAdapter;
public void setConfig(RecyclerView recyclerView,Context context,List<Photo> photos,List<String> keys){
    mContext = context;
    mPhotosAdapter= new PhotosAdapter(photos,keys);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    recyclerView.setAdapter(mPhotosAdapter);
}
 class BookItemView extends RecyclerView.ViewHolder{
private TextView mCategory;
private TextView mUrl;
     private String key;

     public BookItemView(ViewGroup parent) {
         super(LayoutInflater.from(mContext).
                 inflate(R.layout.photo_list_item,parent,false));
         mCategory=(TextView)itemView.findViewById(R.id.category_txtview);
         mUrl=(TextView)itemView.findViewById(R.id.url_txtview);
     }
     public void bind(Photo photo,String key){
         mCategory.setText(photo.getCategory());
         mUrl.setText(photo.getUrl());
         this.key =key;
     }
 }
 class PhotosAdapter extends RecyclerView.Adapter<BookItemView>{
     private List<Photo> mPhotoList;
     private List<String> mKeys;

     public PhotosAdapter(List<Photo> mPhotoList, List<String> mKeys) {
         this.mPhotoList = mPhotoList;
         this.mKeys = mKeys;
     }

     @NonNull
     @Override
     public BookItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         return new BookItemView(parent);
     }

     @Override
     public void onBindViewHolder(@NonNull BookItemView holder, int position) {
holder.bind(mPhotoList.get(position),mKeys.get(position));
     }

     @Override
     public int getItemCount() {
         return mPhotoList.size();
     }
 }
}

