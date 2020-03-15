package com.realllydan.yakira.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realllydan.yakira.R;
import com.realllydan.yakira.data.models.Member;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    private ArrayList<Member> memberArrayList = new ArrayList<>();
    private OnMemberClickListener onMemberClickListener;

    public MemberListAdapter(ArrayList<Member> memberArrayList, OnMemberClickListener onMemberClickListener) {
        this.memberArrayList = memberArrayList;
        this.onMemberClickListener = onMemberClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_member_list_item, parent, false);
        return new ViewHolder(view, onMemberClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setMemberName(memberArrayList.get(position).getName());
        holder.setMemberType(memberArrayList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvMemberName, tvMemberType;
        private OnMemberClickListener onMemberClickListener;

        public ViewHolder(@NonNull View itemView, OnMemberClickListener onMemberClickListener) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvMemberType = itemView.findViewById(R.id.tvMemberType);
            this.onMemberClickListener = onMemberClickListener;
            itemView.setOnClickListener(this);

        }

        void setMemberName(String memberName) {
            tvMemberName.setText(memberName);
        }

        void setMemberType(String memberType) {
            tvMemberType.setText(memberType);
        }

        @Override
        public void onClick(View v) {
            onMemberClickListener.onMemberClick(getAdapterPosition());
        }
    }

    public interface OnMemberClickListener {
        void onMemberClick(int pos);
    }
}
