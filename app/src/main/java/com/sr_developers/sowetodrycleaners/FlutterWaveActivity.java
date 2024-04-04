package com.sr_developers.sowetodrycleaners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FlutterWaveActivity extends AppCompatActivity {

    TextView amount;
    Button btn;
    String Price,Name,Kgs;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_wave);
        getSupportActionBar().setTitle("Confirm Payment");

        amount= findViewById(R.id.amount);
        btn= findViewById(R.id.btn);

        Intent intent = getIntent();
         Price = intent.getStringExtra("price");
        Name = intent.getStringExtra("name");
        Kgs = intent.getStringExtra("kgs");
        amount.setText(Price);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePayment();
            }
        });
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
                Toast.makeText(FlutterWaveActivity.this, "ERROR " + message, Toast.LENGTH_LONG).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(FlutterWaveActivity.this, "CANCELLED ", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveOrder() {
        ProgressDialog progressDialog1 = new ProgressDialog(FlutterWaveActivity.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setTitle("Saving Order");
        progressDialog1.setMessage("Please wait....");
        progressDialog1.show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Orders");

        String time = "" + System.currentTimeMillis();
        HashMap<String, String> OrderData = new HashMap<>();
        OrderData.put("email", user.getEmail());
        OrderData.put("subscribed", "No");
        OrderData.put("method", "Cash");
        OrderData.put("order_Id",time );
        OrderData.put("price", Price);
        OrderData.put("name", Name);
        OrderData.put("status", "Pending");
        OrderData.put("id", firebaseAuth.getUid());
        OrderData.put("kgs", Kgs);

        reference2.child(time).setValue(OrderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(FlutterWaveActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    progressDialog1.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FlutterWaveActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog1.dismiss();
            }
        });


    }

}