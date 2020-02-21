package ru.daniilkryuchkov.timetablekryuchkov;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Post> lessonList = new ArrayList<>();

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_element, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.bind(lessonList.get(position));
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {

        private TextView lessonName;
        private TextView lessonTime;
        private TextView lessonTeaher;
        private TextView lessonPlace;
        private TextView lessonDescription;

        public void bind(Post post){
            lessonName.setText(post.getName());
            lessonTime.setText(post.getStartTime() + " - " + post.getEndTime());
            lessonTeaher.setText(post.getTeacher());
            lessonPlace.setText(post.getPlace());
            lessonDescription.setText(post.getDescription());
        }

        public LessonViewHolder(View itemView) {
            super(itemView);

            lessonName = itemView.findViewById(R.id.lessonName);
            lessonTime = itemView.findViewById(R.id.lessonTime);
            lessonTeaher = itemView.findViewById(R.id.lessonTeacher);
            lessonPlace = itemView.findViewById(R.id.lessonPlace);
            lessonDescription = itemView.findViewById(R.id.lessonDescription);
        }
    }

    public void setItem(Post post) {
        lessonList.add(post);
        notifyDataSetChanged();
    }

    public void clearItems() {
        lessonList.clear();
        notifyDataSetChanged();
    }

}
