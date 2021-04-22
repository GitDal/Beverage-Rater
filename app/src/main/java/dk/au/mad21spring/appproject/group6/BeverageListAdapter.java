package dk.au.mad21spring.appproject.group6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dk.au.mad21spring.appproject.group6.models.Beverage;

public class BeverageListAdapter extends RecyclerView.Adapter<BeverageListAdapter.BeverageViewHolder> {

    public interface IBeverageClickedListener {
        void onBeverageClicked(int index);
    }

    IBeverageClickedListener listener;
    List<Beverage> beverages;

    public BeverageListAdapter(IBeverageClickedListener listener) {
        this.listener = listener;
    }

    public void setCityWeathers(List<Beverage> newList) {
        this.beverages = newList;
        notifyDataSetChanged();
    }

    public class BeverageViewHolder extends RecyclerView.ViewHolder {
        TextView beverageName, beverageCompany, beverageRating;
        ImageView beverageImg;

        public BeverageViewHolder(View itemView) {
            super(itemView);
            beverageName = itemView.findViewById(R.id.listBeverageName);
            beverageCompany = itemView.findViewById(R.id.listBeverageCompany);
            beverageRating = itemView.findViewById(R.id.listBeverageRating);
            beverageImg = itemView.findViewById(R.id.listBeverageImg);
        }
    }

    @Override
    public BeverageListAdapter.BeverageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View beverageView = inflater.inflate(R.layout.beverage_item, parent, false);

        BeverageViewHolder viewHolder = new BeverageViewHolder(beverageView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeverageListAdapter.BeverageViewHolder holder, int position) {
        Beverage beverage = beverages.get(position);

        holder.beverageName.setText(beverage.Name);
        holder.beverageCompany.setText(beverage.CompanyName);
        holder.beverageRating.setText("" + beverage.GlobalRating);

        Glide.with(holder.beverageImg.getContext()).load("https://s3-eu-west-2.amazonaws.com/newzimlive/wp-content/uploads/2019/01/09152727/Fizzy-Drinks.jpg").into(holder.beverageImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBeverageClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (beverages == null) {
            return 0;
        }
        return beverages.size();
    }
}
