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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
    private final int START_MINUTE = 58;
    private final int START_SECOND = 0;

    private long baseTimeMillis;  // 기준 시간 저장
    private Handler handler = new Handler();

    // 1. 선언부에 추가
    private void ComeonSubjectsFromServer() {
        Log.d("SubjectDebug", "📡 ComeonSubjectsFromServer() 호출됨");
        String url = "http://10.0.2.2:8080/get_subjects.jsp";
        Response.Listener<String> responseListener = response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                Log.d("SubjectDebug", "받아온 개수: " + jsonArray.length());

                List<SubjectAdapter.Subject> subjectList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int id = obj.getInt("subject_id");
                    String title = obj.getString("title");
                    int maxSeat = obj.getInt("max_seat");
                    subjectList.add(new SubjectAdapter.Subject(id, title, maxSeat));
                }

                Log.d("SubjectDebug", "subjectList 크기: " + subjectList.size());

                SubjectAdapter adapter = new SubjectAdapter(subjectList);
                rvSubjects.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("SubjectDebug", "파싱 에러: " + e.getMessage());
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, error -> {
            Log.e("SubjectDebug", "Volley 요청 실패: " + error.toString());
            error.printStackTrace();
        });
        queue.add(request);
    }
    public EnrollmentFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);

        tvTime = view.findViewById(R.id.tvTime);
        btnStartTimer = view.findViewById(R.id.btnStartTimer);
        rvSubjects = view.findViewById(R.id.rvSubjects);

        rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));

        // 과목 목록은 향후 서버/DB 연동으로 대체될 예정
        List<SubjectAdapter.Subject> subjectList = new ArrayList<>();
        SubjectAdapter adapter = new SubjectAdapter(subjectList); // 빈 adapter 또는 더미 데이터 연결 가능
        rvSubjects.setAdapter(adapter);

        btnStartTimer.setOnClickListener(v -> {

            long currentClickTime = System.currentTimeMillis();
            double elapsedSec = (currentClickTime - baseTimeMillis) / 1000.0;

            baseTimeMillis = System.currentTimeMillis();
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