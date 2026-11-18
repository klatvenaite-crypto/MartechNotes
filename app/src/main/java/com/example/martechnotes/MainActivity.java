package com.example.martechnotes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText edTopic = (EditText) findViewById(R.id.edTopic);
        EditText edContent = (EditText) findViewById(R.id.edContent);

        Button buttonSave = (Button) findViewById(R.id.btnSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String noteLine = edTopic.getText().toString() + "@" + edContent.getText().toString() + "\n";
                FileManager flManager = new FileManager();
                flManager.writeToFile(noteLine, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Note Saved. Congrats!", Toast.LENGTH_LONG).show();
            }
        });
        Button buttonRefresh = (Button) findViewById(R.id.btnRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ListView listView = findViewById(R.id.lvList);
                    ArrayAdapter<String> adapter;
                    FileManager flManager = new FileManager();
                    ArrayList<String> notes = flManager.readFromFile(getApplicationContext());
                    adapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            notes
                    );

                    listView.setAdapter(adapter);
                }
        });
        Button buttonDelete = (Button) findViewById(R.id.btnDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String topicToDelete = edTopic.getText().toString();
                FileManager flManager = new FileManager();
                ArrayList<String> notes = flManager.readFromFile(getApplicationContext());
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(getApplicationContext().openFileOutput(Constants.Notes_File_Name, Context.MODE_PRIVATE));
                    writer.write("");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < notes.size(); i++) {
                    String note = notes.get(i);
                    if (!note.startsWith(topicToDelete + "@")) {
                        flManager.writeToFile(note + "\n", getApplicationContext());
                    }
                }

                Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_LONG).show();
            }
        });
        Button buttonEdit = (Button) findViewById(R.id.btnEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String topicToEdit = edTopic.getText().toString();
                String newContent = edContent.getText().toString();
                FileManager flManager = new FileManager();
                ArrayList<String> notes = flManager.readFromFile(getApplicationContext());

                // issivalau faila
                try {
                    OutputStreamWriter writer = new OutputStreamWriter(getApplicationContext().openFileOutput(Constants.Notes_File_Name, Context.MODE_PRIVATE));
                    writer.write("");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < notes.size(); i++) {
                    String note = notes.get(i);
                    if (note.startsWith(topicToEdit + "@")) {
                        flManager.writeToFile(topicToEdit + "@" + newContent + "\n", getApplicationContext());
                    } else {
                        flManager.writeToFile(note + "\n", getApplicationContext());
                    }
                }
                Toast.makeText(getApplicationContext(), "Note Edited!", Toast.LENGTH_LONG).show();
            }
        });
    }
}