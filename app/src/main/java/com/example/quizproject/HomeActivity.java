package com.example.quizproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopicAdapter topicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> topics = Arrays.asList("Nature", "Science", "Computer Science");
        topicAdapter = new TopicAdapter(topics, topic -> {
            Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });

        recyclerView.setAdapter(topicAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}