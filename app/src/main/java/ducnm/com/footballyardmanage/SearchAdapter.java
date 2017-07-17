package ducnm.com.footballyardmanage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 7/17/2017.
 */

public class SearchAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    ArrayList<Yard> myList;

    public SearchAdapter(Context context, int layout, ArrayList<Yard> listYard) {
        myContext = context;
        myLayout = layout;
        myList = listYard;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView name = (TextView) convertView.findViewById(R.id.searchName);
        TextView address = (TextView) convertView.findViewById(R.id.searchAddress);

        name.setText(myList.get(position).getYardOwner());
        address.setText(myList.get(position).getAddress());
        Map.Entry<String,String> entry=myList.get(position).getListImage().entrySet().iterator().next();
        String firstValue = entry.getValue();
        Picasso.with(myContext).load(firstValue).into(imageView);
        return convertView;
    }
    private ImageView loadImageViewFromURL(String imgUrl) {
        ImageView image = new ImageView(myContext);
        Picasso.with(myContext).load(imgUrl).into(image);
        return image;
    }
}
