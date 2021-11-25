package com.example.ecommercetemplate.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecommercetemplate.Buyer.HomeActivity;
import com.example.ecommercetemplate.Buyer.MainActivity;
import com.example.ecommercetemplate.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, sportTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, bagsPursesWallets, shoes;
    private ImageView headPhonesHandFree, laptopsPc, watches, smartPhones;

    private Button LogoutBtn, CheckOrdersBtn, crudBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        crudBtn = (Button) findViewById(R.id.crud_products_btn);

        crudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        ImageView[] allImages = {tShirts, sportTShirts, femaleDresses, sweaters,
                glasses, hatsCaps, bagsPursesWallets, shoes,
                headPhonesHandFree, laptopsPc, watches, smartPhones};

        String[] allImagesNames = {"tShirts", "sportTShirts", "femaleDresses", "sweaters",
                "glasses", "hatsCaps", "bagsPursesWallets", "shoes",
                "headPhonesHandFree", "laptopsPc", "watches", "smartPhones"};
 
        for (int i = 0; i < allImages.length; i++)
        {
            String nameOfCategory = allImagesNames[i];
            int id = getResources().getIdentifier(nameOfCategory, "id", this.getPackageName());
            allImages[i] = (ImageView) findViewById(id);
        }

        for (int i = 0; i < allImages.length; i++)
        {
            String category = allImagesNames[i].toUpperCase();
            allImages[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PassCategory(category);
                }
            });
        }
    }

    private void PassCategory(String category)
    {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }
}