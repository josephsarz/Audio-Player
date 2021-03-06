package kulkarni.aditya.audioplayer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by adicool on 16/12/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Song> songArrayList,songArrayListCopy;
    private final songOnClickHandler mClickHandler;

    public SongAdapter(Context context, ArrayList<Song> songArrayList,songOnClickHandler onClickHandler) {
        this.mContext = context;
        this.songArrayList = songArrayList;
        mClickHandler = onClickHandler;
    }

    public void setList(ArrayList<Song> gitHubRepos) {
        this.songArrayList = gitHubRepos;
        songArrayListCopy = new ArrayList<>();
        songArrayListCopy.addAll(songArrayList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.song.setText(songArrayList.get(position).getSong());
        holder.artists.setText(songArrayList.get(position).getArtists());
    }

    @Override
    public int getItemCount() {
        if(songArrayList == null){
            return 0;
        } else {
            return songArrayList.size();
        }
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase(Locale.getDefault());
        Log.d("adapater",searchText);
        songArrayList.clear();

        if(searchText.length() == 0) {
            songArrayList.addAll(songArrayListCopy);
        }
        else{
            Log.d("copylistcheck",songArrayList.toString());
            Log.d("songlistcheck",songArrayList.toString());
            for (int i=0 ; i < songArrayListCopy.size() ; i++){
                if (songArrayListCopy.get(i).getSong().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    Log.d("search hit",songArrayListCopy.get(i).getSong().toLowerCase(Locale.getDefault()));
                    songArrayList.add(songArrayListCopy.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setImage(String imageUrl,ImageView songImage){
		Glide.with(mContext.getApplicationContext())
				.load(imageUrl)
				.listener(new RequestListener<Drawable>()
				{
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
					{
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
					{
						Log.d("Glide", "resource ready");
						return false;
					}

				})
				.into(songImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView song,artists;


        public ViewHolder(View itemView) {
            super(itemView);

            song = (TextView)itemView.findViewById(R.id.song_name);
            artists = (TextView)itemView.findViewById(R.id.song_artists);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Song model = songArrayList.get(getAdapterPosition());
            mClickHandler.onClick(getAdapterPosition(), model.getUrl(),model.getSong(),model.getCover_image());
        }
    }

    public interface songOnClickHandler {
        void onClick(int position, String songUrl,String title,String imageUrl);
    }
}