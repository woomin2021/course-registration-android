// ✅ SubjectAdapter.java
package com.example.pro_test;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final Context context;
    private final String userID;
    private final List<Subject> subjectList;
    private final long baseTimeMillis;
    private boolean userWin;
    private double userTime;
    private int userRank;

    public static class Subject {
        private final int subjectId;
        private final String title;
        private final int maxSeat;

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
        double clickTime;
        boolean isUser;

        public Participant(String name, double clickTime, boolean isUser) {
            this.name = name;
            this.clickTime = clickTime;
            this.isUser = isUser;
        }
    }

    public SubjectAdapter(Context context, String userID, List<Subject> subjectList, long baseTimeMillis) {
        this.context = context;
        this.userID = userID;
        this.subjectList = subjectList;
        this.baseTimeMillis = baseTimeMillis;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.tvSubject.setText(subject.getTitle());
        holder.tvPeople.setText(subject.getMaxSeat() + "명");

        holder.btnApply.setOnClickListener(v -> {
            long currentClickTime = System.currentTimeMillis();
            double elapsedSec = (currentClickTime - baseTimeMillis) / 1000.0;

            List<Participant> list = new ArrayList<>();
            list.add(new Participant("나", elapsedSec, true));

            for (int i = 1; i <= 4; i++) {
                double botTime = 2.0 + Math.random(); // 봇 난이도 조절
                list.add(new Participant("자동봇" + i, botTime, false));
            }

            list.sort((a, b) -> Double.compare(a.clickTime, b.clickTime));

            StringBuilder result = new StringBuilder("[예약 현황]\n");

            userTime = elapsedSec;

            for (int i = 0; i < list.size(); i++) {
                Participant p = list.get(i);
                boolean success = i < 2;

                if (p.isUser) {
                    userWin = success;
                    userRank = i + 1;
                }

                result.append((i + 1)).append("등: ").append(p.name)
                        .append(String.format(" (%.2f초)", p.clickTime))
                        .append(" → ").append(success ? "성공" : "실패");

                if (!success && i == 2) result.append(" (잔여 0석)");
                result.append("\n");
            }

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("수강신청 결과")
                    .setMessage(result.toString())
                    .setPositiveButton("확인", null)
                    .show();

            if (!userWin) {
                Toast.makeText(holder.itemView.getContext(), "신청 실패 – 너무 느렸습니다", Toast.LENGTH_SHORT).show();
            }

            StringRequest request = new StringRequest(Request.Method.POST, "http://10.0.2.2:8080/save_enrollment.jsp",
                    response -> Log.d("EnrollSave", "저장 성공: " + response),
                    error -> Log.e("EnrollSave", "저장 실패: " + error.toString())) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userID", userID);
                    params.put("subject_id", String.valueOf(subject.getSubjectId()));
                    params.put("apply_time", String.format("%.2f", userTime));
                    params.put("result", userWin ? "성공" : "실패");
                    params.put("ranking", String.valueOf(userRank));
                    return params;
                }
            };

            Volley.newRequestQueue(holder.itemView.getContext()).add(request);
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

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