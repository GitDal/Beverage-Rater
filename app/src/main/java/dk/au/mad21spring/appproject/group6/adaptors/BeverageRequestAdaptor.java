package dk.au.mad21spring.appproject.group6.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.BeverageRequest;

public class BeverageRequestAdaptor extends RecyclerView.Adapter<BeverageRequestAdaptor.BeverageRequestViewHolder> {

    public interface IBeverageRequestItemClickedListener {
        void onBeverageRequestClicked(int index);
    }

    private IBeverageRequestItemClickedListener listener;
    private List<BeverageRequest> beverageRequestList;

    public BeverageRequestAdaptor(IBeverageRequestItemClickedListener listener) {
        this.listener = listener;
    }

    public void setBeverageRequests(@NonNull List<BeverageRequest> beverageRequests) {
        this.beverageRequestList = beverageRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeverageRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_item_user, parent, false);
        BeverageRequestViewHolder cvh = new BeverageRequestViewHolder(view, listener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageRequestViewHolder holder, int position) {
        BeverageRequest currentRequest = beverageRequestList.get(position);

        //TODO add glide
        //Glide.with(holder.imgFlag.getContext()).load(curCity.getCountryImageUrl()).into(holder.imgFlag);

        // Add the rest!
        holder.txtBeverageName.setText("Test");
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
            listener.onBeverageRequestClicked(getAdapterPosition());
        }
    }
}