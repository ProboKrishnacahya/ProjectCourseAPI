package com.uc.projectcourseapi.view.CourseView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uc.projectcourseapi.R;
import com.uc.projectcourseapi.helper.SharedPreferenceHelper;
import com.uc.projectcourseapi.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {
    Toolbar toolbar;
    FloatingActionButton btn_add_course;

    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private RecyclerView recyclerView;
    private SharedPreferenceHelper helper;

    public CourseFragment() {
    }

    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle("Courses");
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView = view.findViewById(R.id.rv_course);
        helper = SharedPreferenceHelper.getInstance(requireActivity());
        courseViewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        courseViewModel.init(helper.getAccessToken());
        courseViewModel.getCourses();
        courseViewModel.getResultCourses().observe(getActivity(), showCourse);

        btn_add_course = view.findViewById(R.id.btn_add_course);
        btn_add_course.setOnClickListener(view1 -> {
            NavDirections actions = CourseFragmentDirections.actionCourseFragment2ToAddCourseFragment();
            Navigation.findNavController(view1).navigate(actions);
        });
    }

    List<Course.Courses> results = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    private Observer<Course> showCourse = new Observer<Course>() {
        @Override
        public void onChanged(Course course) {
            results = course.getCourses();
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            courseAdapter = new CourseAdapter(getActivity());
            courseAdapter.setCoursesList(results);
            recyclerView.setAdapter(courseAdapter);
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getViewModelStore().clear();
    }
}