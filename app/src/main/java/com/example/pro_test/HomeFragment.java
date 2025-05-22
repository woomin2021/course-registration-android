package com.example.pro_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnEnroll = view.findViewById(R.id.btnEnroll);
        Button btnTicket = view.findViewById(R.id.btnTicket);
        Button btnMarket = view.findViewById(R.id.btnMarket);

        btnEnroll.setOnClickListener(v -> {
            // 수강신청 Fragment 또는 Activity로 이동
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new EnrollmentFragment());
            transaction.addToBackStack(null); // 뒤로가기 가능하게
            transaction.commit();

        });

        btnTicket.setOnClickListener(v -> {
            // 티켓팅 Fragment 또는 Activity로 이동
        });

        btnMarket.setOnClickListener(v -> {
            // 거래게시판 Fragment 또는 Activity로 이동
        });

        return view;
    }
}