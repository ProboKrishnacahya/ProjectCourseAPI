package com.uc.projectcourseapi.view.CourseView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.uc.projectcourseapi.model.Course;
import com.uc.projectcourseapi.repositories.CourseRepository;

public class CourseViewModel extends AndroidViewModel {
    private CourseRepository courseRepository;
    private static final String TAG = "CourseViewModel";

    public CourseViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token) {
        Log.d(TAG, "init: " + token);
        courseRepository = CourseRepository.getInstance(token);
    }

    private MutableLiveData<Course> resultCourses = new MutableLiveData<>();

    public void getCourses() {
        resultCourses = courseRepository.getCourse();
    }

    public LiveData<Course> getResultCourses() {
        return resultCourses;
    }
}