package com.example.misha.modulea;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.misha.modulea.Link.MyLink;
import java.util.List;



public class LinkAdapter extends ArrayAdapter<MyLink> {

    private Context mContext;
    private int id;
    private List<MyLink> items;
    LayoutInflater lInflater;

    public LinkAdapter(@NonNull Context context, int resource, @NonNull List<MyLink> ar) {
        super(context, resource, ar);
        mContext = context;
        id = resource;
        items = ar;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MyLink getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) { return position; }


    @NonNull
    @Override
    public View getView(int position, View v, @NonNull ViewGroup parent)
    {
        View mView = v ;
        if (mView == null) {
            mView = lInflater.inflate(R.layout.item, parent, false);
        }

        MyLink s = getItem(position);



        TextView text =  mView.findViewById(R.id.tvDescr);

        text.setText(s.getJust_link());
        text.setTextColor(Color.BLACK);


        if(s.getStatus()==1 | s.getStatus()==4 ){
            mView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreenBackground));
        }
        else if(s.getStatus()==2){
            mView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRedBackground));
        }
        else{
            mView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreyBackground));
        }

        return mView;
    }


}

