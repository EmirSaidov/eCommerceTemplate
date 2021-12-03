package com.example.ecommercetemplate.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercetemplate.Interface.ItemClickListener;
import com.example.ecommercetemplate.R;

public class SellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductState;
    public ImageView imageView;
    public ItemClickListener listener;

    public SellerItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image_seller);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name_seller);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description_seller);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price_seller);
        txtProductState = (TextView) itemView.findViewById(R.id.product_state_seller);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }

}