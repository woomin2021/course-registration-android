// EnrollmentFragment.java
package com.example.pro_test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentFragment extends Fragment {

    private TextView tvTime;
    private Button btnStartTimer;
    private RecyclerView rvSubjects;
    private final int START_HOUR = 9;
    private final int START_MINUTE = 59;
    private final int START_SECOND = 58;
    private String userID;

    private long baseTimeMillis = 0;
    private Handler handler = new Handler();

    private void ComeonSubjectsFromServer() {
        String url = "http://10.0.2.2:8080/get_subjects.jsp";

        Volley.newRequestQueue(requireContext()).add(
                new StringRequest(Request.Method.GET, url, response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<SubjectAdapter.Subject> subjectList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int id = obj.getInt("subject_id");
                            String title = obj.getString("title");
                            int maxSeat = obj.getInt("max_seat");
                            subjectList.add(new SubjectAdapter.Subject(id, title, maxSeat));
                        }

                        SubjectAdapter adapter = new SubjectAdapter(requireContext(), userID, subjectList, baseTimeMillis); // 여기 있는거 다 넘겨야 기록이 제대로 넘어감 그래야지 내기록까지 커버 가능 
                        rvSubjects.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("SubjectDebug", "파싱 에러: " + e.getMessage());
                    }
                }, error -> {
                    Log.e("SubjectDebug", "Volley 요청 실패: " + error.toString());
                    error.printStackTrace();
                })
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);

        tvTime = view.findViewById(R.id.tvTime);
        btnStartTimer = view.findViewById(R.id.btnStartTimer);
        rvSubjects = view.findViewById(R.id.rvSubjects);

        rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));

        userID = getActivity().getIntent().getStringExtra("userID");

        btnStartTimer.setOnClickListener(v -> {
            baseTimeMillis = System.currentTimeMillis(); // ✅ 기준 시간 설정
            runTimer();
            ComeonSubjectsFromServer();
        });

        return view;
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedMillis = currentTime - baseTimeMillis;

                int elapsedSeconds = (int) (elapsedMillis / 1000);
                int startInSeconds = START_HOUR * 3600 + START_MINUTE * 60 + START_SECOND;
                int currentTotalSeconds = startInSeconds + elapsedSeconds;

                int hour = currentTotalSeconds / 3600;
                int minute = (currentTotalSeconds % 3600) / 60;
                int second = currentTotalSeconds % 60;

                String time = String.format("%02d:%02d:%02d", hour, minute, second);
                tvTime.setText(time);

                handler.postDelayed(this, 1000);
            }
        });
    }
}