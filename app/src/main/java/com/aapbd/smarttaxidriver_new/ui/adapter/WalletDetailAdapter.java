package com.aapbd.smarttaxidriver_new.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.data.network.model.Transaction;

import java.util.ArrayList;

public class WalletDetailAdapter extends RecyclerView.Adapter<WalletDetailAdapter.MyViewHolder> {

    private ArrayList<Transaction> mTransactionArrayList;
    private Context mContext;

    public WalletDetailAdapter(ArrayList<Transaction> mTransactionArrayList) {
        this.mTransactionArrayList = mTransactionArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wallet_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.description.setText(mTransactionArrayList.get(position).getTransactionDesc());
        if (mTransactionArrayList.get(position).getType().equalsIgnoreCase("C")) {
            holder.amount.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            holder.transfer_type.setText(mContext.getString(R.string.credit));
        } else {
            holder.amount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.transfer_type.setText(mContext.getString(R.string.debit));
        }
        String amount = AppConstant.Currency + " " + MvpApplication.getInstance().getNewNumberFormat(mTransactionArrayList.get(position).getAmount());
        holder.amount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return mTransactionArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView description, transfer_type, amount;

        private MyViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.description);
            transfer_type = view.findViewById(R.id.transfer_type);
            amount = view.findViewById(R.id.amount);

        }
    }
}