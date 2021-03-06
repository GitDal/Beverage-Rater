package dk.au.mad21spring.appproject.group6.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ListIterator;

import dk.au.mad21spring.appproject.group6.R;
import dk.au.mad21spring.appproject.group6.models.db.Beverage;

import com.bumptech.glide.Glide;
import com.google.android.material.color.MaterialColors;

public class BeverageRequestAdapter extends RecyclerView.Adapter<BeverageRequestAdapter.BeverageRequestViewHolder> {

    public interface IBeverageRequestItemClickedListener {
        void onBeverageRequestClicked(int index);
    }

    private static final String TAG = "BeverageRequestAdapter";
    private Context context;
    private IBeverageRequestItemClickedListener listener;
    private List<Beverage> beverageRequestList;
    private int selectedPosition;

    public BeverageRequestAdapter(IBeverageRequestItemClickedListener listener, Context context, int selectedItemPosition) {
        this.listener = listener;
        this.context = context;
        selectedPosition = selectedItemPosition;
    }

    public List<Beverage> getBeverageRequests() { return beverageRequestList; }
    public void setBeverageRequests(@NonNull List<Beverage> beverageRequests) {
        if(beverageRequestList != null && getItemCount() != beverageRequests.size()) {
            //List size changed:
            Log.d(TAG, "setBeverageRequests: List size changed from " + getItemCount() + " to " + beverageRequests.size());
            String id = getSelectedBeverage() != null ? getSelectedBeverage().Id : "";
            this.beverageRequestList = beverageRequests;
            setSelectedBeverage(id);
        } else {
            this.beverageRequestList = beverageRequests;
        }

        if(getSelectedBeverage() == null) {
            selectedPosition = 0;
        }

        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Beverage getSelectedBeverage() {
        if(0 > selectedPosition || selectedPosition > (getItemCount() - 1)) {
            return null;
        }

        return beverageRequestList.get(selectedPosition);
    }

    public void setSelectedBeverage(String requestId) {
        ListIterator<Beverage> it = beverageRequestList.listIterator();
        while(it.hasNext()) {
            Beverage beverage = it.next();
            if(beverage.Id.equals(requestId)) {
                selectedPosition = beverageRequestList.indexOf(beverage);
                return;
            }
        }
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

            holder.txtBeverageName.setTextColor(MaterialColors.getColor(holder.itemView.getRootView(), R.attr.colorPrimary));
        } else {
            holder.txtBeverageName.setTextColor(Color.GRAY);
        }

        Beverage currentRequest = beverageRequestList.get(position);
        holder.txtBeverageName.setText(!currentRequest.Name.isEmpty() ? currentRequest.Name : context.getResources().getString(R.string.default_request_name_new_request));
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
