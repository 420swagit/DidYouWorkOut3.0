package com.example.fanny.didyouworkout30;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{
    EditText editName,editQuantity,editDescription;
    Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=(EditText)findViewById(R.id.editName);
        editQuantity=(EditText)findViewById(R.id.editName);
        editDescription=(EditText)findViewById(R.id.editMarks);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnModify=(Button)findViewById(R.id.btnModify);
        btnView=(Button)findViewById(R.id.btnView);
        btnViewAll=(Button)findViewById(R.id.btnViewAll);
        btnShowInfo=(Button)findViewById(R.id.btnShowInfo);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        db=openOrCreateDatabase("workoutDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS workout(name VARCHAR,quantity VARCHAR,description VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view==btnAdd)
        {
            if(editName.getText().toString().trim().length()==0||
                    editQuantity.getText().toString().trim().length()==0||
                    editDescription.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO workout VALUES('"+editName.getText()+"','"+editQuantity.getText()+
                    "','"+editDescription.getText()+"');");
            showMessage("Success", "Record added");
            clearText();
        }
        if(view==btnDelete)
        {
            if(editName.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE name='"+editName.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM workout WHERE name='"+editName.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid name");
            }
            clearText();
        }
        if(view==btnModify)
        {
            if(editName.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE name='"+editName.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE workout SET quantity='"+editQuantity.getText()+"',description='"+editDescription.getText()+
                        "' WHERE name='"+editName.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else
            {
                showMessage("Error", "Invalid name");
            }
            clearText();
        }
        if(view==btnView)
        {
            if(editName.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE name='"+editName.getText()+"'", null);
            if(c.moveToFirst())
            {
                editQuantity.setText(c.getString(1));
                editDescription.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid name");
                clearText();
            }
        }
        if(view==btnViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM workout", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Name: "+c.getString(0)+"\n");
                buffer.append("Quantity: "+c.getString(1)+"\n");
                buffer.append("Description: "+c.getString(2)+"\n\n");
            }
            showMessage("Workout Details", buffer.toString());
        }
        if(view==btnShowInfo)
        {
            showMessage("Did You Work Out?", "Created by me");
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        editName.setText("");
        editQuantity.setText("");
        editDescription.setText("");
        editName.requestFocus();
    }
}