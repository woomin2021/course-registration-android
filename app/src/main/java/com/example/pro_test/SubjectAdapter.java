package com.example.pro_test;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private long baseTimeMillis;

    // Subject 모델 클래스
    public static class Subject {
        private int subjectId;
        private String title;
        private int maxSeat;

        public Subject(int subjectId, String title, int maxSeat) {
            this.subjectId = subjectId;
            this.title = title;
            this.maxSeat = maxSeat;
        }

        public int getSubjectId() { return subjectId; }
        public String getTitle() { return title; }
        public int getMaxSeat() { return maxSeat; }
    }
    public static class Participant {
        String name;
        double clickTime; // 초 단위
        boolean isUser;

        public Participant(String name, double clickTime, boolean isUser) {
            this.name = name;
            this.clickTime = clickTime;
            this.isUser = isUser;
        }
    }
    private final List<Subject> subjectList;

    // 외부에서 과목 리스트를 전달받음 우리가 더미로 하면 간단하게 끝나는데 디비에서 받아야 해서 생성자 생성 해줘야 해
    public SubjectAdapter(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.tvSubject.setText(subject.getTitle());
        holder.tvPeople.setText(subject.getMaxSeat() + "명");
        // 수강 신청 버튼 클릭 처리 (경쟁 시뮬레이션 로직 여기에 작성 예정)

        holder.btnApply.setOnClickListener(v -> {
            long currentClickTime = System.currentTimeMillis();
            double elapsedSec = (currentClickTime - baseTimeMillis) / 1000.0;

            List<Participant> list = new ArrayList<>();
            list.add(new Participant("나", elapsedSec, true));

            // 봇 4명 생성 (0.3초 ~ 1.5초 랜덤)
            for (int i = 1; i <= 4; i++) {
                double botTime = 0.3 + Math.random() * 1.2;
                list.add(new Participant("자동봇" + i, botTime, false));
            }

            // 반응 시간 빠른 순 정렬
            list.sort((a, b) -> Double.compare(a.clickTime, b.clickTime));

            // 결과 문자열 만들기
            StringBuilder result = new StringBuilder();
            result.append("[예약 현황]\n");

            int successCount = 0;
            boolean userSucceeded = false;

            for (int i = 0; i < list.size(); i++) {
                Participant p = list.get(i);
                boolean success = i < 2; // 상위 2명만 성공
                if (p.isUser && success) userSucceeded = true;

                result.append((i + 1)).append("등: ").append(p.name)
                        .append(String.format(" (%.2f초)", p.clickTime))
                        .append(" → ").append(success ? "성공" : "실패");

                if (!success && i == 2) result.append(" (잔여 0석)");
                result.append("\n");
            }

            // 결과 출력
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("수강신청 결과")
                    .setMessage(result.toString())
                    .setPositiveButton("확인", null)
                    .show();

            // 실패한 사용자에겐 Toast
            if (!userSucceeded) {
                Toast.makeText(holder.itemView.getContext(),
                        "신청 실패 – 너무 느렸습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    // ViewHolder
    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvPeople;
        Button btnApply;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvPeople = itemView.findViewById(R.id.tvPeople);
            btnApply = itemView.findViewById(R.id.btnApply);
        }
    }
}