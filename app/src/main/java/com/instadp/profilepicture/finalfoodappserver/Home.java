package com.instadp.profilepicture.finalfoodappserver;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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
import com.instadp.profilepicture.finalfoodappserver.MainActivity;
import com.instadp.profilepicture.finalfoodappserver.Model.Category;
import com.instadp.profilepicture.finalfoodappserver.Model.Food;
import com.instadp.profilepicture.finalfoodappserver.R;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.MenuViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
TextView fullname;
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseRecyclerAdapter<Category, com.instadp.profilepicture.finalfoodappserver.ViewHolder.MenuViewHolder> adapter;
    //View

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    MaterialEditText adtname;
    Button select,upload;
    Category newCategory;
    Uri saveUri;
    DrawerLayout drawer;
    private  final int PICK_IMAGE_REQUEST=71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);
        changeStatusBarColor("WHITE");
        status_icon_color();
        //int Firebase
        database=FirebaseDatabase.getInstance();
        categories=database.getReference("Category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toolbar.setTitleTextColor(Color.BLACK);
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);
        fullname= (TextView) headerView.findViewById(R.id.user_name_display);
        fullname.setText(Common.currentUser.getName());
        //init view
        recycler_menu= (RecyclerView) findViewById(R.id.recycle_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View ass_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        adtname= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtName);

        select= (Button) ass_menu_layout.findViewById(R.id.button_select);
        upload= (Button) ass_menu_layout.findViewById(R.id.button_upload);

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

if(newCategory!=null)
{
    categories.push().setValue(newCategory);
    Snackbar.make(drawer,"New Category"+newCategory.getName()+" was added",Snackbar.LENGTH_SHORT).show();
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
                    Toast.makeText(Home.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //set value for new caategory
                        newCategory=new Category(adtname.getText().toString(),uri.toString());
                    }
                });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null&&data.getData()!=null)
        {
         saveUri=data.getData();
         select.setText("Image selected");
        }
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    private void loadMenu() {

        adapter =new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,categories) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.texMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
//                final Category clickitem=model;

                viewHolder.setItemClickListner(new ItemOnClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent i=new Intent(Home.this,MainActivity.class);
//                        i.putExtra("CategoryId",adapter.getRef(position).getKey());
//                        Toast.makeText(Home.this, adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
//                        startActivity(i);
//                        // Toast.makeText(Home.this, ""+clickitem.getName(), Toast.LENGTH_SHORT).show();
                    //code late
                        Intent foodlist=new Intent(Home.this,FoodList.class);
                        foodlist.putExtra("CategoryId",adapter.getRef(position).getKey()+"");
                        startActivity(foodlist);



                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {

            Intent order=new Intent(Home.this,OrderStatus.class);
            startActivity(order);
        } else if (id == R.id.nav_signout) {

        } else if (id == R.id.nev_menu) {

        } else if (id == R.id.nev_cart) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//update
    //


    @Override
    public boolean onContextItemSelected(MenuItem item) {
     if(item.getTitle().equals(Common.UPDATE))
     {
         showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

     }
     else if(item.getTitle().equals(Common.DELETE))
     {
         deleteCategory(adapter.getRef(item.getOrder()).getKey());


     }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        categories.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, final Category item) {


        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View ass_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        adtname= (MaterialEditText) ass_menu_layout.findViewById(R.id.edtName);
        select= (Button) ass_menu_layout.findViewById(R.id.button_select);
        upload= (Button) ass_menu_layout.findViewById(R.id.button_upload);
adtname.setText(item.getName());
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

              item.setName(adtname.getText().toString());
                categories.child(key).setValue(item);

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


    private void changeImage(final Category item) {
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
                    Toast.makeText(Home.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    void status_icon_color(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (true) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }

    }

}
