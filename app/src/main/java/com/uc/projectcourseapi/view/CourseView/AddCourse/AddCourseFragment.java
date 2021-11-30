package com.uc.projectcourseapi.view.CourseView.AddCourse;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.uc.projectcourseapi.R;
import com.uc.projectcourseapi.helper.SharedPreferenceHelper;
import com.uc.projectcourseapi.model.Course;

public class AddCourseFragment extends Fragment {
    Toolbar toolbar;

    TextInputLayout courseCode, courseTitle, courseLecturer, courseSks, courseDesc;
    Button btn_submit_course;

    private AddCourseViewModel addCourseViewModel;
    private SharedPreferenceHelper helper;
    private static final String TAG = "AddCourseFragment";

    public AddCourseFragment() {
    }

    public static AddCourseFragment newInstance(String param1, String param2) {
        AddCourseFragment fragment = new AddCourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Add Course");

        courseCode = view.findViewById(R.id.course_code_input);
        courseTitle = view.findViewById(R.id.course_title_input);
        courseLecturer = view.findViewById(R.id.course_lecturer_input);
        courseSks = view.findViewById(R.id.course_sks_input);
        courseDesc = view.findViewById(R.id.course_desc_input);
        btn_submit_course = view.findViewById(R.id.btn_submit_course);

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        addCourseViewModel = new ViewModelProvider(getActivity()).get(AddCourseViewModel.class);
        addCourseViewModel.init(helper.getAccessToken());

        btn_submit_course.setOnClickListener(view1 -> {
            if (!courseCode.getEditText().getText().toString().isEmpty()
                    && !courseTitle.getEditText().getText().toString().isEmpty()
                    && !courseLecturer.getEditText().getText().toString().isEmpty()
                    && !courseSks.getEditText().getText().toString().isEmpty()
                    && !courseDesc.getEditText().getText().toString().isEmpty()) {
                String code = courseCode.getEditText().getText().toString().trim();
                String title = courseTitle.getEditText().getText().toString().trim();
                String lecturer = courseLecturer.getEditText().getText().toString().trim();
                String sks = courseSks.getEditText().getText().toString().trim();
                String desc = courseDesc.getEditText().getText().toString().trim();

                Course.Courses courses = addCourseData(code, title, lecturer, sks, desc);
                addCourseViewModel.createCourse(courses).observe(requireActivity(), courses1 -> {
                    if (courses1 != null) {
                        NavDirections actions = AddCourseFragmentDirections.actionAddCourseFragmentToCourseFragment2();
                        Navigation.findNavController(view1).navigate(actions);
                        Toast.makeText(requireActivity(), "Add Course Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), "Add Course Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireActivity(), "All field must not empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Course.Courses addCourseData(String code, String title, String lecturer, String sks, String desc) {
        Course.Courses courses = new Course.Courses(code, title, lecturer, Integer.parseInt(sks), desc);
        return courses;
    }
}