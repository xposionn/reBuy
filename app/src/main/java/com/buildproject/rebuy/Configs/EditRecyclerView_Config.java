package com.buildproject.rebuy.Configs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buildproject.rebuy.Modules.ListOfItems;
import com.buildproject.rebuy.R;
import com.buildproject.rebuy.Services.FirebaseDBadapterUsers;

import java.util.List;

public class EditRecyclerView_Config {
    private Context mContext;
    private UsersAdapter mItemAdapter;
    private String list_id;
    private String path;
    private ListOfItems.Permission permission;


    public void setConfig(RecyclerView recyclerView, Context context, List<String> users, List<String> keys, String list_id, String path, ListOfItems.Permission permission){
        mContext = context;
        this.path = path;
        mItemAdapter = new UsersAdapter(users, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mItemAdapter);
        this.list_id = list_id;
        this.permission = permission;

    }


    class EditorView extends RecyclerView.ViewHolder{
        private TextView mUserName;
        private ImageButton mDeleteItem;

        private String key; //item id record

        public EditorView(ViewGroup parent){
            super(LayoutInflater.from(mContext) //parent constructor
                    .inflate(R.layout.partner, parent,false));
            mUserName = (TextView) itemView.findViewById(R.id.user_id_editor);
            mDeleteItem = (ImageButton) itemView.findViewById(R.id.delete_editor);

        }

        private void setElements(final String user) {
            //mUserName.setText(user);
            new FirebaseDBadapterUsers().setNameByUesrID(user, mUserName);

            mDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (permission== ListOfItems.Permission.EDITOR || permission== ListOfItems.Permission.VIEWER || permission== ListOfItems.Permission.NOTHING) {
                        //TODO make toast
//                        makeText(this, mContext.getString(R.string.cannot_delete_item), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new FirebaseDBadapterUsers(list_id).deleteItem(path, key,
                                new FirebaseDBadapterUsers.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<String> lists, List< String > keys) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {

                                    }

                                    @Override
                                    public void DataIsDeleted() {
                                        //TODO write log
                                    }
                                });
                    }
                }
            });
        }


        public void bind(final String user, final String key, View.OnClickListener listener){
            setElements(user);
            itemView.setOnClickListener(listener);
            this.key = key;
        }

        public void bind(final String user, final String key){
            setElements(user);
            this.key = key;
        }
    }

    class UsersAdapter extends RecyclerView.Adapter<EditorView>{
        private List<String> mUser;
        private List<String> mKeys;
//        private String list_id;
//
//        public void setList_id(String list_id) {
//            this.list_id = list_id;
//        }

        public UsersAdapter(List<String> mUser, List<String> mKeys) {
            this.mUser = mUser;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public EditorView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EditorView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EditorView holder, final int position) {
            holder.bind(mUser.get(position), mKeys.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent newIntent = new Intent(view.getContext(), EditItemActivity.class);
                   // newIntent.putExtra("item_info",mItem.get(position));
                    newIntent.putExtra("permission",permission);
                    newIntent.putExtra("item",mItem.get(position));
                    newIntent.putExtra("list_id",list_id);
                    newIntent.putExtra("itemKey",mKeys.get(position));

                    view.getContext().startActivity(newIntent);*/
                    Toast.makeText(mContext, "pressed on user.", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mUser.size();
        }
    }
}
