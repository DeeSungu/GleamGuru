package com.sr_developers.sowetodrycleaners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sr_developers.sowetodrycleaners.adapters.AdapterOrders;
import com.sr_developers.sowetodrycleaners.connectors.noders;
import com.sr_developers.sowetodrycleaners.models.OrderDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class LaundaryActivty extends AppCompatActivity {
    private Dialog dialog;
    private EditText name, price, kgs;
    ProgressDialog progressDialog;
    private FloatingActionButton upgrade;

    private RecyclerView recyclerView;
    private TextView tvData,date,submit,back;
    private String Name, Price, Kgs,Date;

    private AdapterOrders adapterTips;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private ProgressBar progress_circular;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundary_activty);
        getSupportActionBar().setTitle("General Clothes Cleaning");

        upgrade = findViewById(R.id.fab_add);
        progressDialog = new ProgressDialog(LaundaryActivty.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        dialog = new Dialog(LaundaryActivty.this);


        linearLayoutManager = new LinearLayoutManager(LaundaryActivty.this);
        dialog = new Dialog(LaundaryActivty.this);
        recyclerView = findViewById(R.id.recyclerView);
        tvData = findViewById(R.id.tvData);

        progress_circular = findViewById(R.id.progress_circular);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        orderDetailsArrayList = new ArrayList<>();

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.activity_add_laundary);
                dialog.setCancelable(false);
                dialog.show();
                kgs = dialog.findViewById(R.id.kgs);
                date = dialog.findViewById(R.id.date);
                submit = dialog.findViewById(R.id.button1);
                price = dialog.findViewById(R.id.price);
                name = dialog.findViewById(R.id.name);
                back = dialog.findViewById(R.id.back);
                LinearLayout one = dialog.findViewById(R.id.general);
                one.setVisibility(View.VISIBLE);


                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting
                        // our day, month and year.
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                LaundaryActivty.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                            },
                            year, month, day);
                        datePickerDialog.show();
                        }

                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Name = name.getText().toString().trim();
                        Kgs = kgs.getText().toString().trim();
                        Price = price.getText().toString().trim();
                        Date = date.getText().toString().trim();


                        if (Name.isEmpty()) {
                            Toast.makeText(LaundaryActivty.this, "Enter Item Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Kgs.isEmpty()) {
                            Toast.makeText(LaundaryActivty.this, "Please Specify Weight/Quantity/Size", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Price.isEmpty()) {
                            Toast.makeText(LaundaryActivty.this, "Enter Total Price", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Date.isEmpty()) {
                            Toast.makeText(LaundaryActivty.this, "Enter Date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Dialog dialog = new Dialog(LaundaryActivty.this);
                        dialog.setContentView(R.layout.payment_selector);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        RadioButton google = dialog.findViewById(R.id.cash);
                        RadioButton mobile = dialog.findViewById(R.id.mobile);
                        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                if (google.isChecked()) {
                                    dialog.dismiss();

                                    saveData(Name, Kgs, Price,Date);
                                    progressDialog = new ProgressDialog(LaundaryActivty.this);
                                    progressDialog.setMessage("Please wait..");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                } else if (mobile.isChecked()) {
                                    dialog.dismiss();

                                    MakePayment();
//                                    Intent intent = new Intent(LaundaryActivty.this, FlutterWaveActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("price", Price);
//                                    bundle.putString("name", Name);
//                                    bundle.putString("kgs", Price);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);

                                }
                            }
                        });


                    }
                });

            }
        });

        getData();
    }

    private void MakePayment() {
        FirebaseUser user;
        user= FirebaseAuth.getInstance().getCurrentUser();
        new RaveUiManager(this)
                .setAmount(Double.parseDouble(Price))
                .setCountry("Kenya")
                .setEmail(user.getEmail())
                .setCurrency("KES")
                .setfName("Soweto Dry Cleaners")
                .setNarration("")
                .setPublicKey("FLWPUBK-81db65949d76be055b83dff88702ed54-X")
                .setEncryptionKey("9c87784fb22565b359ee737d")
                .setTxRef(System.currentTimeMillis() + "Ref")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptBarterPayments(true)
                .acceptBankTransferPayments(true)
                .acceptMpesaPayments(true)
                .acceptGHMobileMoneyPayments(true)
                .acceptUgMobileMoneyPayments(true)
                .acceptZmMobileMoneyPayments(true)
                .acceptRwfMobileMoneyPayments(true)
                .acceptSaBankPayments(true)
                .acceptUkPayments(true)
                .acceptUssdPayments(true)
                .acceptAchPayments(true)
                .onStagingEnv(false)
                .shouldDisplayFee(true)
                .showStagingLabel(true)
                .initialize();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {

                saveOrder();

            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(LaundaryActivty.this, "ERROR " + message, Toast.LENGTH_LONG).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(LaundaryActivty.this, "CANCELLED ", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveOrder() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String time = "" + System.currentTimeMillis();
        HashMap<String, String> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("subscribed", "No");
        data.put("method", "Mobile Transfer");
        data.put("order_Id", time);
        data.put("name", Name);
        data.put("kgs", Kgs);
        data.put("price", Price);
        data.put("date", Date);
        data.put("status", "Pending");
        data.put("id", firebaseAuth.getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(noders.SERVICE_NODE);
        reference.child(noders.GENERAL).child(time).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(LaundaryActivty.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LaundaryActivty.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
    private void saveData(String name, String kgs, String price, String date) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String time = "" + System.currentTimeMillis();
        HashMap<String, String> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("subscribed", "No");
        data.put("method", "Cash");
        data.put("order_Id", time);
        data.put("name", name);
        data.put("kgs", kgs);
        data.put("price", price);
        data.put("date", date);
        data.put("status", "Pending");
        data.put("id", firebaseAuth.getUid());


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(noders.SERVICE_NODE);
        reference.child(noders.GENERAL).child(time).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(LaundaryActivty.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LaundaryActivty.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void getData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(noders.SERVICE_NODE);

        reference.child(noders.GENERAL).orderByChild("id").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    orderDetailsArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OrderDetails tips = snapshot.getValue(OrderDetails.class);
                        orderDetailsArrayList.add(tips);

                    }
                    if (orderDetailsArrayList.isEmpty()) {
                        progress_circular.setVisibility(View.GONE);
                        tvData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        progress_circular.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        tvData.setVisibility(View.GONE);
                    }
                    adapterTips = new AdapterOrders(LaundaryActivty.this, orderDetailsArrayList);
                    recyclerView.setAdapter(adapterTips);
                } else {
                    progress_circular.setVisibility(View.GONE);
                    tvData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}