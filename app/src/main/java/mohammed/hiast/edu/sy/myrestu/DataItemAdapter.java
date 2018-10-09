package mohammed.hiast.edu.sy.myrestu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mohammed.hiast.edu.sy.myrestu.model.DataItem;
import mohammed.hiast.edu.sy.myrestu.utils.ImageCacheManager;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    public static final String ITEM_KEY ="ITEM_KEY" ;
    private List<DataItem> mItems;
    private Context mContext;
    private static final String PHOTOS_BASE_URL =
                  "https://mohammed.ugar-it.com/services/images/";
    //private Map<String,Bitmap> bitmapMap = new HashMap<>();
    public static final String ITEM_ID ="ITEM_ID" ;
    private SharedPreferences.OnSharedPreferenceChangeListener changeListener;

    public DataItemAdapter(Context context, List<DataItem> items ) {

        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);


        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mContext);

        boolean isGrid =
                settings.getBoolean(
                        mContext.getString(R.string.display_in_grid_pref),false);

        int layout = isGrid ? R.layout.grid_layout: R.layout.list_item;

        changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("Prefernces" , "Prefernce Change item : "+ key);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(changeListener);
        View itemView = inflater.inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);

        try {
            holder.tvName.setText(item.getItemName());
//            String imageFile = item.getImage();
//            InputStream inputStream = mContext.getAssets().open(imageFile);
//            Drawable d = Drawable.createFromStream(inputStream, null);
//            holder.imageView.setImageDrawable(d);

//            Bitmap bitmap = bitmapMap.get(item.getItemName());
//            if(bitmap!=null)
//                    holder.imageView.setImageBitmap(bitmap);

//            if(bitmapMap.containsKey(item.getItemName())){
//            Bitmap bitmap = bitmapMap.get(item.getItemName());
//            holder.imageView.setImageBitmap(bitmap);
//            }else{
//                ImageDownloader task = new ImageDownloader();
//                task.setViewHolder(holder);
//                task.execute(item);
//            }

//            Bitmap bitmap = ImageCacheManager.getBitmap(mContext,item);
//            if(bitmap!=null){
//                holder.imageView.setImageBitmap(bitmap);
//            }else{
//                ImageDownloader task = new ImageDownloader();
//                task.setViewHolder(holder);
//                task.execute(item);
//            }

            String imageUrl = PHOTOS_BASE_URL+item.getImage();

            Picasso.get()
                    .load(imageUrl)
                    .into(holder.imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "you click " + item.getItemName(), Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(mContext,DetailActivity.class);
                intent.putExtra(ITEM_ID,item);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "you long clicked " + item.getItemName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }

//    private  class ImageDownloader extends AsyncTask<DataItem,Void,Bitmap>{
//
//        private static final String PHOTOS_BASE_URL =
//                "https://mohammed.ugar-it.com/services/images/";
//        private  DataItem currentDataItem;
//        private ViewHolder mHolder;
//
//        public void setViewHolder(ViewHolder view){
//            this.mHolder = view;
//        }
//        @Override
//        protected Bitmap doInBackground(DataItem... dataItems) {
//            this.currentDataItem = dataItems[0];
//            String imageUrl = PHOTOS_BASE_URL + currentDataItem.getImage();
//
//            InputStream in = null;
//            try {
//                in = (InputStream)new URL(imageUrl).getContent();
//                Bitmap bitmap = BitmapFactory.decodeStream(in);
//                return bitmap;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if(in!=null)
//                        in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            mHolder.imageView.setImageBitmap(bitmap);
////            bitmapMap.put(currentDataItem.getItemName(),
////                    bitmap);
//            ImageCacheManager.putBitmap(mContext,currentDataItem,bitmap);
//        }
//    }
}