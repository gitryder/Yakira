package com.realllydan.yakira.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.realllydan.yakira.R;
import com.realllydan.yakira.data.models.Member;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    private ArrayList<Member> memberArrayList = new ArrayList<>();
    private OnMemberClickListener onMemberClickListener;
    private OnCallClickListener onCallClickListener;

    public MemberListAdapter(ArrayList<Member> memberArrayList, OnMemberClickListener onMemberClickListener, OnCallClickListener onCallClickListener) {
        this.memberArrayList = memberArrayList;
        this.onMemberClickListener = onMemberClickListener;
        this.onCallClickListener = onCallClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_member_list_item, parent, false);
        return new ViewHolder(view, onMemberClickListener, onCallClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setMemberName(memberArrayList.get(position).getName());
        holder.setMemberType(memberArrayList.get(position).getType());
        //holder.setLastCall(memberArrayList.get(position).getLastCall(), memberArrayList.get(position).getLastCallMadeBy());
    }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvMemberName, tvMemberType, tvLastCallMade;
        private MaterialButton bCallMember;
        private OnMemberClickListener onMemberClickListener;
        private OnCallClickListener onCallClickListener;

        public ViewHolder(@NonNull View itemView, OnMemberClickListener onMemberClickListener, OnCallClickListener onCallClickListener) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvMemberType = itemView.findViewById(R.id.tvMemberType);
            tvLastCallMade = itemView.findViewById(R.id.tvLastCallMade);
            bCallMember = itemView.findViewById(R.id.bCallMember);

            this.onMemberClickListener = onMemberClickListener;
            this.onCallClickListener = onCallClickListener;

            itemView.setOnClickListener(this);
            bCallMember.setOnClickListener(this);
        }

        void setMemberName(String memberName) {
            tvMemberName.setText(memberName);
        }

        void setMemberType(String memberType) {
            tvMemberType.setText(memberType);
        }

        void setLastCall(String lastCall, String lastCallMadeBy) {
            if (lastCall != null && lastCallMadeBy != null) {
                tvLastCallMade.setVisibility(View.VISIBLE);
                tvLastCallMade.setText(lastCall + " • " + lastCallMadeBy);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bCallMember) {
                onCallClickListener.onCallClick(getAdapterPosition());
            } else {
                onMemberClickListener.onMemberClick(getAdapterPosition());
            }
        }
    }

    public interface OnMemberClickListener {
        void onMemberClick(int pos);
    }

    public interface OnCallClickListener {
        void onCallClick(int pos);
    }
}
