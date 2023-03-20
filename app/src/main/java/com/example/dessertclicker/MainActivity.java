package com.example.dessertclicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.dessertclicker.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int revenue = 0;
    private int dessertsSold = 0;
    public static final String KEY_REVENUE = "revenue_key";
    public static final String KEY_DESSERT_SOLD = "dessert_sold_key";
    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    public class Dessert {
        private final int imageId;
        private final int price;
        private final int startProductionAmount;
        public Dessert(int imageId, int price, int startProductionAmount) {
            this.imageId = imageId;
            this.price = price;
            this.startProductionAmount = startProductionAmount;
        }
        public int getImageId() {
            return imageId;
        }
        public int getPrice() {
            return price;
        }
        public int getStartProductionAmount() {
            return startProductionAmount;
        }
    }
    private final List<Dessert> allDesserts = Arrays.asList(
            new Dessert(R.drawable.cupcake, 5, 0),
            new Dessert(R.drawable.donut, 10, 5),
            new Dessert(R.drawable.eclair, 15, 20),
            new Dessert(R.drawable.froyo, 30, 50),
            new Dessert(R.drawable.gingerbread, 50, 100),
            new Dessert(R.drawable.honeycomb, 100, 200),
            new Dessert(R.drawable.icecreamsandwich, 500, 500),
            new Dessert(R.drawable.jellybean, 1000, 1000),
            new Dessert(R.drawable.kitkat, 2000, 2000),
            new Dessert(R.drawable.lollipop, 3000, 4000),
            new Dessert(R.drawable.marshmallow, 4000, 8000),
            new Dessert(R.drawable.nougat, 5000, 16000),
            new Dessert(R.drawable.oreo, 6000, 20000)
    );
    private Dessert currentDessert = allDesserts.get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Called");
        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.dessertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDessertClicked();
            }
        });

        // Set the TextViews to the right values
        binding.setRevenue(revenue);
        binding.setAmountSold(dessertsSold);

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.getImageId());

        if (savedInstanceState != null) {

            revenue = savedInstanceState.getInt(KEY_REVENUE, 0);
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0);
            binding.setRevenue(revenue);
            binding.setAmountSold(dessertsSold);
            showCurrentDessert();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Called");
        outState.putInt(KEY_REVENUE, revenue);
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold);
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private void onDessertClicked() {

        // Update the score
        revenue += currentDessert.getPrice();
        dessertsSold++;

        binding.setRevenue(revenue);
        binding.setAmountSold(dessertsSold);

        // Show the next dessert
        showCurrentDessert();
    }

    /**
     * Determine which dessert to show.
     */
    private void showCurrentDessert() {
        // revenue
        // dessertsSold
        Dessert newDessert = allDesserts.get(0);
        for (Dessert dessert : allDesserts) {
            if (dessertsSold >= dessert.getStartProductionAmount()) {
                newDessert = dessert;
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break;
        }
        // If the new dessert is actually different than the current dessert, update the image
        if (!newDessert.equals(currentDessert)) {
            currentDessert = newDessert;
            binding.dessertButton.setImageResource(newDessert.getImageId());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onStop Called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart Called");
    }

    private void onShare() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setText(getString(R.string.share_text, dessertsSold, revenue)).setType("text/plain").getIntent();
        try {
            startActivity(shareIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.sharing_not_available), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareMenuButton:
                onShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}