package com.example.ecommercetemplate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommercetemplate.R;
import com.example.ecommercetemplate.Seller.SellerCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminCrudProductsActivity extends AppCompatActivity {

    private Button crudBtn, deleteBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crud_products);

        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference()
                .child("Products")
                .child(productID);

        crudBtn = findViewById(R.id.crud_product_btn);
        name = findViewById(R.id.crud_product_name);
        price = findViewById(R.id.crud_product_price);
        description = findViewById(R.id.crud_product_description);
        imageView = findViewById(R.id.crud_product_image);
        deleteBtn = findViewById(R.id.delete_product_btn);

        displayProductInfo();

        crudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCrudProductsActivity.this);
                builder.setTitle("You are about to delete this product, are you sure?");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                        {
                            deleteProduct();
                        }
                        else
                        {
                            finish();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void deleteProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminCrudProductsActivity.this, SellerCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminCrudProductsActivity.this, "The Product has been deleted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (TextUtils.isEmpty(pName))
        {
            Toast.makeText(this, "Name is Required!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice))
        {
            Toast.makeText(this, "Price is Required!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pDescription))
        {
            Toast.makeText(this, "Description is Required!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",pDescription);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminCrudProductsActivity.this,  "Product updated successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminCrudProductsActivity.this, SellerCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displayProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    name.setText(pPrice);
                    name.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}