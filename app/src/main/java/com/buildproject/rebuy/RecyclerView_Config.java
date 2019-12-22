package com.buildproject.rebuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class RecyclerView_Config {
    private Context mContext;

    class ListItemView extends RecyclerView.ViewHolder{
        private TextView mListTitleName;
        private TextView mListOwnerName;
        private TextView mListPriority;

        private String key; //list id record

        public ListItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.listofitems_minified_for_recycleviews, parent,false));
            mListTitleName = (TextView) itemView.findViewById(R.id.list_titleName);
            mListOwnerName = (TextView) itemView.findViewById(R.id.list_ownerName);
            mListPriority = (TextView) itemView.findViewById(R.id.list_priorityText);

        }

    }
}
