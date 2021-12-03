package com.example.ecommercetemplate.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercetemplate.Admin.AdminApproveNewProducts;
import com.example.ecommercetemplate.Buyer.MainActivity;
import com.example.ecommercetemplate.Model.Products;
import com.example.ecommercetemplate.R;
import com.example.ecommercetemplate.ViewHolder.ProductViewHolder;
import com.example.ecommercetemplate.ViewHolder.SellerItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference nonVerifiedProductsRef;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = (item) -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                startActivity(intentHome);
                return true;

            case R.id.navigation_add:
                Intent intentCat = new Intent(SellerHomeActivity.this, SellerCategoryActivity.class);
                startActivity(intentCat);
                return true;

            case R.id.navigation_logout:
                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        nonVerifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_recycler   );
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(nonVerifiedProductsRef.orderByChild("sid")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, SellerItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerItemViewHolder holder, int position, @NonNull Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price: "+ model.getPrice()+"$");
                        holder.txtProductState.setText("State: "+ model.getProductState());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String productId = model.getPid();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to delete this product?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0)
                                        {
                                            DeleteSellerProduct(productId);
                                        }
                                        if(which == 1)
                                        {

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        SellerItemViewHolder holder = new SellerItemViewHolder(v);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void DeleteSellerProduct(String productId) {
        nonVerifiedProductsRef.child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerHomeActivity.this, "This product has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}