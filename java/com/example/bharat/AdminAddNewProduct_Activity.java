package com.example.bharat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProduct_Activity extends AppCompatActivity {

    private String CategoryName, Pname, Pdescription, Pprice, Pseller, Pcountry, saveCurrentDate, saveCurrentTime;
    private Button Add_new_product_btn;
    private EditText Input_product_name, Input_product_description, Input_product_price, Input_product_seller, Input_product_country;
    private ImageView Input_product_image;
    private static final int GalleryPick = 1;
    private Uri Imageuri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product_);

        CategoryName = getIntent().getExtras().get("Category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Productimages");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Add_new_product_btn = (Button) findViewById(R.id.add_new_product);
        Input_product_image = (ImageView) findViewById(R.id.select_product_image);
        Input_product_name = (EditText) findViewById(R.id.product_name);
        Input_product_description = (EditText) findViewById(R.id.product_description);
        Input_product_price = (EditText) findViewById(R.id.product_price);
        Input_product_seller = (EditText) findViewById(R.id.product_seller);
        Input_product_country = (EditText) findViewById(R.id.product_country);
        loadingBar = new ProgressDialog(this);

        Input_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        Add_new_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery() {
        Intent Galleryintent = new Intent();
        Galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        Galleryintent.setType("image/");
        startActivityForResult(Galleryintent, GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            Imageuri = data.getData();
            Input_product_image.setImageURI(Imageuri);
        }
    }

    private void ValidateProductData()
    {
        Pname = Input_product_name.getText().toString();
        Pdescription = Input_product_description.getText().toString();
        Pprice = Input_product_price.getText().toString();
        Pseller = Input_product_seller.getText().toString();
        Pcountry = Input_product_country.getText().toString();

        if (Imageuri == null)
        {
            Toast.makeText(this, "Product Image is mandatory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Product Name is mandatory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pdescription))
        {
            Toast.makeText(this, "Product Description is mandatory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pprice))
        {
            Toast.makeText(this, "Product Price is mandatory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pseller))
        {
            Toast.makeText(this, "Seller Name is mandatory", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(Pcountry) && Pcountry != "INDIA")
        {
            Toast.makeText(this, "Product Country name INDIA is mandatory", Toast.LENGTH_SHORT).show();
        }

        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Dear Admin, PLease have patience, while the product Credentials are being added...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = ProductImagesRef.child(Imageuri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(Imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProduct_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProduct_Activity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProduct_Activity.this, "Image traced Successfully...", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {
        HashMap<String , Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", Pname);
        productMap.put("description", Pdescription);
        productMap.put("price", Pprice);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("seller name", Pseller);
        productMap.put("Country Name", Pcountry);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProduct_Activity.this, "Product is listed successfully...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminAddNewProduct_Activity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }

                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProduct_Activity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}