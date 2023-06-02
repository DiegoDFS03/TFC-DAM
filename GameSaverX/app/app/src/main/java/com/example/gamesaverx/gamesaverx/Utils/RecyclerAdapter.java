package com.example.gamesaverx.gamesaverx.Utils;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Interfaces.OnOfferClickListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private List<Offer> items;

    private OnOfferClickListener listener;

    public RecyclerAdapter(List<Offer> items, OnOfferClickListener listener) {
        this.items = items;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_list, parent, false);
        return new RecyclerHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        final Offer item = items.get(position);
        // Obtener los valores de original_price y discount_percentage
        String originalPrice = item.getOriginal_price();
        String discountPercentage = item.getDiscount_percentage();

        BigDecimal noriginalPrice = new BigDecimal(originalPrice);
        BigDecimal ndiscountPercentage = new BigDecimal(discountPercentage);

        // Multiplicar original_price y discount_percentage para obtener el precio con descuento
        BigDecimal discountedPrice = noriginalPrice.multiply(ndiscountPercentage.divide(BigDecimal.valueOf(100)));

        //Obtener la fecha actual y la fecha de final de oferta
        LocalDate currentDate = LocalDate.now();
        String endDate = item.getEnd_date();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate end_date = LocalDate.parse(endDate, formatter);
        long durationInDays = ChronoUnit.DAYS.between(currentDate, end_date);

        //Pasar a String las variables anteriores

        String discounted_price = discountedPrice.toString();
        String time_left = String.valueOf(durationInDays);


        Picasso.get().load(item.getImage()).into(holder.gameImage);
        holder.gameTitle.setText(item.getTitle());
        holder.storeName.setText(item.getStore());
        holder.original_price.setText(item.getOriginal_price());
        holder.percentage_discount.setText(item.getDiscount_percentage()+"%");
        holder.discount_price.setText(discounted_price);
        holder.time_left.setText(time_left+" días restantes");
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
    public class RecyclerHolder extends RecyclerView.ViewHolder{
        private ImageView gameImage;
        private TextView gameTitle,percentage_discount,discount_price,original_price,storeName,time_left;
        public RecyclerHolder(@NonNull View itemView_1) {
            super(itemView_1);
            gameImage= itemView.findViewById(R.id.gameImage);
            gameTitle= itemView.findViewById(R.id.gameTitle);
            percentage_discount= itemView.findViewById(R.id.percentage_discount);
            discount_price= itemView.findViewById(R.id.discount_price);
            original_price= itemView.findViewById(R.id.original_price);
            storeName= itemView.findViewById(R.id.storeName);
            time_left= itemView.findViewById(R.id.time_left);

        }
    }
}
