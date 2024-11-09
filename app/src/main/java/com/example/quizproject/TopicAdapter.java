package com.example.quizproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final List<String> topics;
    private final OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(String topic);
    }

    public TopicAdapter(List<String> topics, OnTopicClickListener listener) {
        this.topics = topics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        String topic = topics.get(position);
        holder.topicName.setText(topic);
        holder.itemView.setOnClickListener(v -> listener.onTopicClick(topic));
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicName;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.topic_name);
        }
    }
}