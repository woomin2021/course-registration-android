package com.example.pro_test;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pro_test.R;
import com.example.pro_test.RecordAdapter;
import com.example.pro_test.RecordItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<RecordItem> recordList;
    private String userID;

    @Nullable

    //데이터 리스트를 받아- 각각을 XML에 바인딩해서 화면에 출력 하는거 애는 출력 전용 프라그먼트는 데이터 소스를 리사이클러뷰랑 연결
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        userID = getActivity().getIntent().getStringExtra("userID");


        recyclerView = view.findViewById(R.id.rvRecord);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recordList = new ArrayList<>();
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        String url = "http://10.0.2.2:8080/get_my_enrollment.jsp?userID=" + userID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                recordList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String date = obj.getString("date");
                    String result = obj.getString("result");
                    int rank = obj.getInt("ranking");
                    float time = (float) obj.getDouble("apply_time");

                    recordList.add(new RecordItem(date, "수강신청", result, rank, time));
                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        });

        Volley.newRequestQueue(requireContext()).add(request);

        return view;
    }
}