package ru.daniilkryuchkov.timetablekryuchkov;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONFitnessKitApi {
    @GET("/schedule/get_group_lessons_v2/1/")
    public Call<List<Post>> getGroupLessons();
}
