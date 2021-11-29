package com.uc.projectcourseapi.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.uc.projectcourseapi.model.Course;
import com.uc.projectcourseapi.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {

    private static CourseRepository courseRepository;
    private RetrofitService apiService;
    private static final String TAG = "CourseRepository";

    private CourseRepository(String token) {
        Log.d(TAG, "token: " + token);
        apiService = RetrofitService.getInstance(token);
    }

    public static CourseRepository getInstance(String token) {
        if (courseRepository == null) {
            courseRepository = new CourseRepository(token);
        }
        return courseRepository;
    }

    public synchronized void resetInstance() {
        if (courseRepository == null) {
            courseRepository = null;
        } else {
            courseRepository = null;
        }
    }

    public MutableLiveData<Course> getCourse() {
        final MutableLiveData<Course> listCourse = new MutableLiveData<>();

        apiService.getCourse().enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse" + response.body().getCourses().size());
                        listCourse.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });

        return listCourse;
    }
}