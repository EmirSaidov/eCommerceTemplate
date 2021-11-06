package com.example.ecommercetemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, sportTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, bagsPursesWallets, shoes;
    private ImageView headPhonesHandFree, laptopsPc, watches, smartPhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

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