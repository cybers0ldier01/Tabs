package com.example.misha.modulea;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;


//Кастомный адаптер для возможности менять цветик в ячеечках
public class LinkAdapter<String> extends ArrayAdapter<String> {

    private Context mContext;
    private int id;
    private String[] items ;
    Context ctx;
    LayoutInflater lInflater;

    public LinkAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        mContext = context;
        id = resource;
        items = objects;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return items.length;
    }

    // элемент по позиции
    @Override
    public String getItem(int position) {
        return items[position];
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View v, @NonNull ViewGroup parent) // методик, который меняет вид нашей вьюшечки
    {
        View mView = v ;
        if (mView == null) {
            mView = lInflater.inflate(R.layout.item, parent, false);
        }

        String s = getItem(position);
        TextView text = (TextView) mView.findViewById(R.id.tvDescr);

        text.setText((CharSequence) s);
        text.setTextColor(Color.BLACK);

        //а вот тут собсн и происходит установка цвета бэкграунда по статусу ссылки (пока что стрингов)
        if(s.equals("Петр")){
            mView.setBackgroundColor(Color.RED);
        }
        else if(s.equals("Анна")){
            mView.setBackgroundColor(Color.GREEN);
        }
        else{
            mView.setBackgroundColor(Color.GRAY);
        }





        return mView;
    }


}

