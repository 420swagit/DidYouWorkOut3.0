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
    EditText editName,editQuantity,editMarks;
    Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo,btnStreak;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editQuantity=(EditText)findViewById(R.id.editRollno);
        editName=(EditText)findViewById(R.id.editName);
        editMarks=(EditText)findViewById(R.id.editMarks);
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
        btnStreak.setOnClickListener(this);
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS workout(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view==btnAdd)
        {
            if(editQuantity.getText().toString().trim().length()==0|| editName.getText().toString().trim().length()==0|| editMarks.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO workout VALUES('"+ editQuantity.getText()+"','"+editName.getText()+
                    "','"+editMarks.getText()+"');");
            showMessage("Success", "Excercise added");
            clearText();
        }
        if(view==btnDelete)
        {
            if(editQuantity.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter workout name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE rollno='"+ editQuantity.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM workout WHERE rollno='"+ editQuantity.getText()+"'");
                showMessage("Success", "Exercise Deleted");
            }
            else
            {
                showMessage("Error", "Invalid Name");
            }
            clearText();
        }
        if(view==btnModify)
        {
            if(editQuantity.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter exercise name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE rollno='"+editQuantity.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE workout SET name='"+editName.getText()+"',marks='"+editMarks.getText()+
                        "' WHERE rollno='"+editQuantity.getText()+"'");
                showMessage("Success", "Workout Modified");
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        if(view==btnView)
        {
            if(editQuantity.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter exercise name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM workout WHERE rollno='"+editQuantity.getText()+"'", null);
            if(c.moveToFirst())
            {
                editName.setText(c.getString(1));
                editMarks.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid workout");
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
                buffer.append("Excercise Name: "+c.getString(0)+"\n");
                buffer.append("Excercise Description: "+c.getString(1)+"\n");
                buffer.append("Excercise Quantity: "+c.getString(2)+"\n\n");
            }
            showMessage("Excercise Details", buffer.toString());
        }
        if(view==btnShowInfo)
        {
            showMessage("App", "data");
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
        editQuantity.setText("");
        editName.setText("");
        editMarks.setText("");
        editQuantity.requestFocus();
    }
}