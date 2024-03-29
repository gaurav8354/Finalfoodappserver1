package com.instadp.profilepicture.finalfoodappserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.Model.Category;
import com.instadp.profilepicture.finalfoodappserver.Model.Food;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.FoodViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout rootLayout;
    FloatingActionButton fab;
    FirebaseDatabase db;
    DatabaseReference foodlist;
    FirebaseStorage storage;
    StorageReference storageReference;
    String categoryId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    Uri saveUri;
    Food newfood;
    private  final int PICK_IMAGE_REQUEST=71;
   MaterialEditText edtName,edtDiscription,edtPrice,edtDiscount;
    Button select,upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        rootLayout= (RelativeLayout) findViewById(R.id.rootLayout);
    db=FirebaseDatabase.getInstance();
        foodlist=db.getReference("Food");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        recyclerView= (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });
if (getIntent()!=null)
{
    categoryId=getIntent().getStringExtra("CategoryId");
    if(!categoryId.isEmpty())
    {
        loadListFood(categoryId);}
    else
    {
        Toast.makeText(this, "null recieved", Toast.LENGTH_SHORT).show();
    }
}
    }
    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Add new Food");
        alertDialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View ass_menu_layout=inflater.inflate(R.layout.add_new_food_layout,null);
        edtName= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtName);
        edtDiscription= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtDescription);
        edtPrice= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtDiscount);
        select= (Button) ass_menu_layout.findViewById(R.id.button_select);
        upload= (Button) ass_menu_layout.findViewById(R.id.button_upload);
        //upload things
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        alertDialog.setView(ass_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(newfood!=null)
                {
                    foodlist.push().setValue(newfood);
                      Snackbar.make(findViewById(R.id.rootLayout),"New Category"+newfood.getName()+" was added",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void loadListFood(String categoryId) {
    adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
            Food.class,
    R.layout.food_item,FoodViewHolder.class,
            foodlist.orderByChild("menuId").equalTo(categoryId)
            ) {
        @Override
        protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
             viewHolder.foodName.setText(model.getName());
            Picasso.with(getBaseContext())
                    .load(model.getImage())
                    .into(viewHolder.foodView);
            viewHolder.setItemClickListner(new ItemOnClickListner() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    //code late
                }
            });

        }
    };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {
        if(saveUri!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new caategory
                            newfood=new Food();
                            newfood.setName(edtName.getText().toString());
                            newfood.setDescription(edtDiscription.getText().toString());
                            newfood.setPrice(edtPrice.getText().toString());
                            newfood.setDiscount(edtDiscount.getText().toString());
                            newfood.setMenuId(categoryId);
                            newfood.setImage(uri.toString());
                                }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_IMAGE_REQUEST&&resultCode==RESULT_OK
                &&data!=null &&data.getData()!=null)
        {
            saveUri=data.getData();
            select.setText("Image Selected");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
           showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }
    private void deleteFood(String key) {
        foodlist.child(key).removeValue();
    }
    private void showUpdateFoodDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Edit new Food");
        alertDialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View ass_menu_layout=inflater.inflate(R.layout.add_new_food_layout,null);
        edtName= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtName);
        edtDiscription= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtDescription);
        edtPrice= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtDiscount);
        select= (Button) ass_menu_layout.findViewById(R.id.button_select);
        upload= (Button) ass_menu_layout.findViewById(R.id.button_upload);
        edtName.setText((item.getName()));
        edtDiscount.setText((item.getDiscount()));
        edtPrice.setText((item.getPrice()));
        edtDiscription.setText((item.getDescription()));
        //upload things
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        alertDialog.setView(ass_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setDescription(edtDiscription.getText().toString());
                    foodlist.child(key).setValue(item);
               //  foodlist.push().setValue(newfood);
//               Snackbar.make(findViewById(R.id.rootLayout),"Food"+newfood.getName()+" was edited",Snackbar.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void changeImage(final Food item) {
        if(saveUri!=null)
        {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new caategory
                            item.setImage(uri.toString());
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+progress+"%");
                        }
                    });


        }
    }
}
