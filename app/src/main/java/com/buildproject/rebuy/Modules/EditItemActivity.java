package com.buildproject.rebuy.Modules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buildproject.rebuy.ActivityList;
import com.buildproject.rebuy.Enums.Priority;
import com.buildproject.rebuy.MainActivity;
import com.buildproject.rebuy.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    ItemInList current_item;

    EditText item_name;

    EditText quantity;
    ImageButton down;
    ImageButton up;

    ImageButton green_priority;
    ImageButton yellow_priority;
    ImageButton red_priority;
    Priority priority;

    CheckBox is_bought;

    TextView added_by;
    TextView added_at;
    EditText notes;

    pl.droidsonroids.gif.GifImageView barcode_button;

    Button apply_button;

    //TODO put this static variable in another class
    static DateFormat date_time_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item_name = findViewById(R.id.edit_item_name);
        quantity = findViewById(R.id.quantity);
        down = findViewById(R.id.imageButton_down);
        up = findViewById(R.id.imageButton_up);

        green_priority = findViewById(R.id.imageButton_green);
        yellow_priority = findViewById(R.id.imageButton_yellow);
        red_priority = findViewById(R.id.imageButton_red);

        is_bought = findViewById(R.id.isbought);
        added_by = findViewById(R.id.add_by);
        added_at = findViewById(R.id.changed_at);
        notes = findViewById(R.id.edit_notes);
        barcode_button = findViewById(R.id.barcode);
        apply_button = findViewById(R.id.edit_item_apply);

        Bundle bundle = getIntent().getExtras();
        current_item = (ItemInList)bundle.get("item_info");
        if (current_item == null) {
            current_item = new ItemInList();
            //TODO current_item.setAddedBy(getUserName);
        }

        else {
            item_name.setText(current_item.getItemName());
            added_by.setText((CharSequence) current_item.getAddedBy());
            added_at.setText(date_time_format.format(new Date()));
            if (!current_item.getNotes().isEmpty())
                notes.setText(current_item.getNotes());
        }

        quantity.setText(current_item.getQuantity());
        setPriority(current_item.getPriority());
        is_bought.setChecked(current_item.isBought());
    }

    public void clickDown(View view) {
        int i = Integer.parseInt(quantity.getText().toString());
        if (i>0) //minimum quantity is 0
            quantity.setText(i-1);
    }

    public void clickUp(View view) {
        int i = Integer.parseInt(quantity.getText().toString());
        quantity.setText(++i);
    }

    public void clickGreen(View view) {
        setPriority(Priority.LOW);
    }

    public void clickYellow(View view) {
        setPriority(Priority.MID);
    }

    public void clickRed(View view) {
        setPriority(Priority.HIGH);
    }

    private void setPriority(Priority prio) {
        priority = prio;
        //TODO change something in images of priority
    }

    public void onBarcodeClick(View view) {
        //TODO open scanner activity and delete this stupid toast
        CharSequence text = "Thank you!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    public void saveItem (View view) {
        if (current_item.getAddedTime() == null)
            current_item.setAddedTime(new Date());
        current_item.setBought(is_bought.isChecked());
        current_item.setItemName(item_name.getText().toString());
        current_item.setNotes(notes.getText().toString());
        current_item.setPriority(priority);
        current_item.setQuantity(Integer.parseInt(quantity.getText().toString()));
        //TODO push current_item()

        //go to list activity
        Intent toList = new Intent(getBaseContext(), ActivityList.class);
        startActivity(toList);
    }
}
