package view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            viewHolder.setbg = (LinearLayout)convertView.findViewById(R.id.setbg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(video.getName());
        int i = video.getId()%7;

        switch (i){
            case 0:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg0);
                break;
            case 1:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg1);
                break;
            case 2:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg2);
                break;
            case 3:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg3);
                break;
            case 4:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg4);
                break;
            case 5:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg5);
                break;
            case 6:
                viewHolder.setbg.setBackgroundResource(R.drawable.bg6);
                break;
            default:
                viewHolder.setbg.setBackgroundResource(R.drawable.corner);
                break;
        }
        return convertView;
    }
}

class ViewHolder{
    TextView name;
    ShineButton like;
    ImageView delete;
    LinearLayout setbg;
}
