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
        if (courseRepository != null) {
            courseRepository = null;
        } else {
            courseRepository = null;
        }
    }

    public MutableLiveData<Course> getCourses() {
        final MutableLiveData<Course> listCourses = new MutableLiveData<>();

        apiService.getCourses().enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse" + response.body());
                        listCourses.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return listCourses;
    }

    public MutableLiveData<Course> getCourseDetail(String code) {
        final MutableLiveData<Course> listCourseDetail = new MutableLiveData<>();

        apiService.getCourseDetail(code).enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse" + response.body());
                        listCourseDetail.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return listCourseDetail;
    }

    public MutableLiveData<Course.Courses> createCourse(Course.Courses courses) {
        final MutableLiveData<Course.Courses> listAddCourse = new MutableLiveData<>();

        apiService.createCourses(courses).enqueue(new Callback<Course.Courses>() {
            @Override
            public void onResponse(Call<Course.Courses> call, Response<Course.Courses> response) {
                Log.d(TAG, "onResponse: " + response.body());
                listAddCourse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Course.Courses> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return listAddCourse;
    }
}