package dk.au.mad21spring.appproject.group6.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.Beverage;

import com.bumptech.glide.Glide;

public class BeverageRequestAdapter extends RecyclerView.Adapter<BeverageRequestAdapter.BeverageRequestViewHolder> {

    public interface IBeverageRequestItemClickedListener {
        void onBeverageRequestClicked(int index);
    }

    private IBeverageRequestItemClickedListener listener;
    private List<Beverage> beverageRequestList;
    private int selectedPosition; //first element in list

    public BeverageRequestAdapter(IBeverageRequestItemClickedListener listener, int selectedItemPosition) {
        this.listener = listener;
        selectedPosition = selectedItemPosition;
    }

    public void setBeverageRequests(@NonNull List<Beverage> beverageRequests) {
        this.beverageRequestList = beverageRequests;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @NonNull
    @Override
    public BeverageRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item, parent, false);
        BeverageRequestViewHolder cvh = new BeverageRequestViewHolder(view, listener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageRequestViewHolder holder, int position) {
        if(selectedPosition == position) {
            holder.txtBeverageName.setTextColor(Color.GREEN);
        } else {
            holder.txtBeverageName.setTextColor(Color.GRAY);
        }

        Beverage currentRequest = beverageRequestList.get(position);
        holder.txtBeverageName.setText(currentRequest.Name);
        holder.txtBeverageCompanyName.setText(currentRequest.CompanyName);
        Glide.with(holder.imgBeverage.getContext()).load(currentRequest.ImageUrl).into(holder.imgBeverage);
        holder.imgStatusIcon.setImageResource(currentRequest.Status.resolveIconResId());
    }

    @Override
    public int getItemCount() {
        if(beverageRequestList == null) {
            return 0;
        }

        return beverageRequestList.size();
    }

    public class BeverageRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgBeverage, imgStatusIcon;
        TextView txtBeverageName, txtBeverageCompanyName;

        IBeverageRequestItemClickedListener listener;

        public BeverageRequestViewHolder(@NonNull View itemView, IBeverageRequestItemClickedListener beverageRequestClickedListener) {
            super(itemView);

            imgBeverage = itemView.findViewById(R.id.requestListItemUserBeverageImg);
            imgStatusIcon = itemView.findViewById(R.id.requestListItemUserStatusIcon);
            txtBeverageName = itemView.findViewById(R.id.requestListItemUserBeverageNameTxt);
            txtBeverageCompanyName = itemView.findViewById(R.id.requestListItemUserCompanyNameTxt);
            listener = beverageRequestClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedPosition);
            selectedPosition = getLayoutPosition();
            notifyItemChanged(selectedPosition);

            listener.onBeverageRequestClicked(getAdapterPosition());
        }
    }
}
