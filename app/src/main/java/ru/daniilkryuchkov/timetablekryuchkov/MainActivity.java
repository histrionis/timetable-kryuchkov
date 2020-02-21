package ru.daniilkryuchkov.timetablekryuchkov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lessonsRecyclerViewMonday;
    private RecyclerView lessonsRecyclerViewTuesday;
    private RecyclerView lessonsRecyclerViewWednesday;
    private RecyclerView lessonsRecyclerViewThursday;
    private RecyclerView lessonsRecyclerViewFriday;
    private RecyclerView lessonsRecyclerViewSaturday;
    private RecyclerView lessonsRecyclerViewSunday;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        lessonsRecyclerViewMonday = findViewById(R.id.lessonsListMonday);
        lessonsRecyclerViewMonday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewTuesday = findViewById(R.id.lessonsListTuesday);
        lessonsRecyclerViewTuesday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewWednesday = findViewById(R.id.lessonsListWednesday);
        lessonsRecyclerViewWednesday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewThursday = findViewById(R.id.lessonsListThursday);
        lessonsRecyclerViewThursday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewFriday = findViewById(R.id.lessonsListFriday);
        lessonsRecyclerViewFriday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewSaturday = findViewById(R.id.lessonsListSaturday);
        lessonsRecyclerViewSaturday.setLayoutManager(new LinearLayoutManager(this));

        lessonsRecyclerViewSunday = findViewById(R.id.lessonsListSunday);
        lessonsRecyclerViewSunday.setLayoutManager(new LinearLayoutManager(this));

        List<LessonAdapter> lessonAdaptersList = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            lessonAdaptersList.add(new LessonAdapter());
        }

        lessonsRecyclerViewMonday.setAdapter(lessonAdaptersList.get(0));
        lessonsRecyclerViewTuesday.setAdapter(lessonAdaptersList.get(1));
        lessonsRecyclerViewWednesday.setAdapter(lessonAdaptersList.get(2));
        lessonsRecyclerViewThursday.setAdapter(lessonAdaptersList.get(3));
        lessonsRecyclerViewFriday.setAdapter(lessonAdaptersList.get(4));
        lessonsRecyclerViewSaturday.setAdapter(lessonAdaptersList.get(5));
        lessonsRecyclerViewSunday.setAdapter(lessonAdaptersList.get(6));

        SQLiteDatabase myDB = dbHelper.getWritableDatabase();

        NetworkService.getInstance()
                .getJSONApi()
                .getGroupLessons()
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                        List<Post> posts = response.body();

                        ContentValues myCV = new ContentValues();
                        myDB.delete("lessons", null, null);

                        for (Post post : posts) {
                            lessonAdaptersList.get(post.getWeekDay() - 1).setItem(post);

                            myCV.put("name", post.getName());
                            myCV.put("startTime", post.getStartTime());
                            myCV.put("endTime", post.getEndTime());
                            myCV.put("teacher", post.getTeacher());
                            myCV.put("place", post.getPlace());
                            myCV.put("description", post.getDescription());
                            myCV.put("weekDay", post.getWeekDay());
                            myDB.insert("lessons", null, myCV);
                        }
                        dbHelper.close();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                        Cursor myCursor = myDB.query("lessons", null, null, null, null, null, null);

                        if (myCursor.moveToFirst()) {

                            int idColIndex = myCursor.getColumnIndex("id");
                            int nameColIndex = myCursor.getColumnIndex("name");
                            int startTimeColIndex = myCursor.getColumnIndex("startTime");
                            int endTimeColIndex = myCursor.getColumnIndex("endTime");
                            int teacherColIndex = myCursor.getColumnIndex("teacher");
                            int placeColIndex = myCursor.getColumnIndex("place");
                            int descriptionColIndex = myCursor.getColumnIndex("description");
                            int weekDayColIndex = myCursor.getColumnIndex("weekDay");

                            List<Post> posts = new ArrayList<>();

                            do {
                                Post tmpPost = new Post();

                                tmpPost.setName(myCursor.getString(nameColIndex));
                                tmpPost.setStartTime(myCursor.getString(startTimeColIndex));
                                tmpPost.setEndTime(myCursor.getString(endTimeColIndex));
                                tmpPost.setTeacher(myCursor.getString(teacherColIndex));
                                tmpPost.setPlace(myCursor.getString(placeColIndex));
                                tmpPost.setDescription(myCursor.getString(descriptionColIndex));
                                tmpPost.setWeekDay(myCursor.getInt(weekDayColIndex));

                                posts.add(tmpPost);
                            } while (myCursor.moveToNext());

                            for (Post post : posts) {
                                lessonAdaptersList.get(post.getWeekDay() - 1).setItem(post);

                            }

                        } else {
                            myCursor.close();
                        }
                        Log.d("MY_LOG","Ошибка во время загрузки данных");
                    }
                });
    }
}
