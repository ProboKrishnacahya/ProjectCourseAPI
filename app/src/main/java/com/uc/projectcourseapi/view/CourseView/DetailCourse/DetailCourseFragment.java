package com.uc.projectcourseapi.view.CourseView.DetailCourse;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.uc.projectcourseapi.R;
import com.uc.projectcourseapi.helper.SharedPreferenceHelper;
import com.uc.projectcourseapi.model.Course;
import com.uc.projectcourseapi.view.CourseView.CourseViewModel;

public class DetailCourseFragment extends Fragment {
    Toolbar toolbar;
    TextView detailCourseCode, detailCourseTitle, detailCourseLecturer,
            detailCourseSks, detailCourseDesc;
    Button btn_edit, btn_delete;

    private static final String TAG = "DetailCourseFragment";

    private CourseViewModel courseViewModel;
    private SharedPreferenceHelper helper;

    public DetailCourseFragment() {
        // Required empty public constructor
    }

    public static DetailCourseFragment newInstance(String param1, String param2) {
        DetailCourseFragment fragment = new DetailCourseFragment();
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
        return inflater.inflate(R.layout.fragment_detail_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar_main);
        toolbar.setTitle("Detail Course");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources()
                .getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        detailCourseCode = view.findViewById(R.id.detail_course_code);
        detailCourseTitle = view.findViewById(R.id.detail_course_title);
        detailCourseLecturer = view.findViewById(R.id.detail_course_lecturer);
        detailCourseDesc = view.findViewById(R.id.detail_course_description);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_delete = view.findViewById(R.id.btn_delete);

        helper = SharedPreferenceHelper.getInstance(requireActivity());
        courseViewModel = new ViewModelProvider(getActivity()).get(CourseViewModel.class);
        courseViewModel.init(helper.getAccessToken());
        String code = getArguments().getString("course_code");
        Log.d(TAG, "course_code: " + code);
        courseViewModel.getCourseDetail(code);
        courseViewModel.getResultCourseDetail().observe(getActivity(), showCourseDetail);

        btn_edit.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("course_code", code);
            Navigation.findNavController(view1).navigate(R.id.action_detailCourseFragment_to_addCourseFragment, bundle);
        });

        btn_delete.setOnClickListener(view12 -> {
            courseViewModel.deleteCourse(code);
            NavDirections actions = DetailCourseFragmentDirections.actionDetailCourseFragmentToAddCourseFragment2();
            Navigation.findNavController(view12).navigate(actions);
        });
    }

    private Observer<Course> showCourseDetail = new Observer<Course>() {
        @Override
        public void onChanged(Course course) {
            Course.Courses resultCourse = course.getCourses().get(0);
            if (course == null) {
                detailCourseCode.setText("Unknown");
                detailCourseTitle.setText("Unknown");
                detailCourseLecturer.setText("Unknown");
                detailCourseSks.setText("Unknown");
                detailCourseDesc.setText("Unknown");
            } else {
                String codeCourse = resultCourse.getCourse_code();
                String title = resultCourse.getCourse_name();
                String lecturer = resultCourse.getLecturer();
                int sks = resultCourse.getNumber_sks();
                String desc = resultCourse.getDescription();

                detailCourseCode.setText(codeCourse);
                detailCourseTitle.setText(title);
                detailCourseLecturer.setText(lecturer);
                detailCourseSks.setText(String.valueOf(sks));
                detailCourseDesc.setText(desc);
            }
        }
    };
}