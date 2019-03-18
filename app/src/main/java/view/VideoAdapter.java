package view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.player.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

import database.Video;

public class VideoAdapter extends ArrayAdapter<Video> {

    private int resourceId;
    private Context mContext;
    private LayoutInflater mInflater;

    public VideoAdapter(@NonNull Context context, int resource, @NonNull List<Video> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Video video = (Video)getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.videoitem,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.like = (ShineButton)convertView.findViewById(R.id.like);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(video.getName());
        return convertView;
    }
}

class ViewHolder{
    TextView name;
    ShineButton like;
    ImageView delete;
}
