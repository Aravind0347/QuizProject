package com.example.quizproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, scoreTracker, timerText;
    private RadioGroup answersGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private FirebaseFirestore db;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.question_text);
        scoreTracker = findViewById(R.id.score_tracker);
        timerText = findViewById(R.id.timer);
        answersGroup = findViewById(R.id.answers_group);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        db = FirebaseFirestore.getInstance();
        questions = new ArrayList<>();

        String topic = getIntent().getStringExtra("TOPIC");
        fetchQuestions(topic);

        // Set OnClickListener for each RadioButton
        answer1.setOnClickListener(this::onAnswerSelected);
        answer2.setOnClickListener(this::onAnswerSelected);
        answer3.setOnClickListener(this::onAnswerSelected);
        answer4.setOnClickListener(this::onAnswerSelected);
    }

    private void fetchQuestions(String topic) {
        db.collection(topic)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questions.add(document.toObject(Question.class));
                        }
                        Collections.shuffle(questions);
                        if (questions.size() >= 5) {
                            questions = questions.subList(0, 5); // Get 5 random questions
                        }
                        displayNextQuestion();
                    } else {
                        // Handle the error
                        Toast.makeText(this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < 5) {
            Question question = questions.get(currentQuestionIndex);
            questionText.setText(question.getQuestionText());
            List<String> answers = question.getAnswers();
            Collections.shuffle(answers);
            answer1.setText(answers.get(0));
            answer2.setText(answers.get(1));
            answer3.setText(answers.get(2));
            answer4.setText(answers.get(3));

            answersGroup.clearCheck();
            startTimer();
        } else {
            // Quiz finished
            Toast.makeText(this, "Quiz finished! Your score: " + score, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(QuizActivity.this, ScoreSummaryActivity.class);
            intent.putExtra("CORRECT_ANSWERS", score);
            intent.putExtra("TOTAL_QUESTIONS", questions.size());
            startActivity(intent);
            finish();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time left: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                markUnanswered();
            }
        }.start();
    }

    private void markUnanswered() {
        currentQuestionIndex++;
        displayNextQuestion();
    }

    private void checkAnswer(String selectedAnswer) {
        Question question = questions.get(currentQuestionIndex);
        if (question.getCorrectAnswer().equals(selectedAnswer)) {
            score++;
            scoreTracker.setText("Score: " + score);
            Log.d("QuizActivity", "Correct answer! Score: " + score);
        }
        currentQuestionIndex++;
        displayNextQuestion();
    }

    private void onAnswerSelected(View view) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        RadioButton selectedRadioButton = (RadioButton) view;
        if (selectedRadioButton != null) {
            Log.d("QuizActivity", "Selected answer: " + selectedRadioButton.getText().toString());
            checkAnswer(selectedRadioButton.getText().toString());
        } else {
            // Handle the case where no RadioButton is selected
            Log.d("QuizActivity", "No answer selected, marking unanswered");
            markUnanswered();
        }
    }



}