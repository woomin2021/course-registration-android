package com.example.pro_test;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro_test.R;
import com.example.pro_test.RecordAdapter;
import com.example.pro_test.RecordItem;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<RecordItem> recordList;

    @Nullable
    @Override
    //데이터 리스트를 받아- 각각을 XML에 바인딩해서 화면에 출력 하는거 애는 출력 전용 프라그먼트는 데이터 소스를 리사이클러뷰랑 연결
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        recyclerView = view.findViewById(R.id.rvRecord);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 예시 데이터
        recordList = new ArrayList<>();
        recordList.add(new RecordItem("2025-05-07", "수강신청", "성공", 2, 0.51f));
        recordList.add(new RecordItem("2025-05-08", "티켓팅", "성공", 1, 0.30f));

        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}