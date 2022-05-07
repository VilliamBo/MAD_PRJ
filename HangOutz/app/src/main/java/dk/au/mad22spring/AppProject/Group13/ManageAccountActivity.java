package dk.au.mad22spring.AppProject.Group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.au.mad22spring.AppProject.Group13.viewmodel.ManageAccountViewModel;

public class ManageAccountActivity extends AppCompatActivity {

    private Button btnBackToMain, btnDeleteAccount, btnUpdateAccount;
    private EditText edtName;
    private ManageAccountViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        vm = new ViewModelProvider(this).get(ManageAccountViewModel.class);

        setupUI();

    }

    private void setupUI(){

        edtName = findViewById(R.id.edtUpdateUserName);

        btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(view ->{
            finish();
        });

        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(view ->{
            vm.deleteAccount();
            Intent i = new Intent(ManageAccountActivity.this, LoginActivity.class);
            startActivity(i);
        });

        btnUpdateAccount = findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(view ->{
            String imgUrl = "";
            vm.updateUser(edtName.getText().toString());
            Toast.makeText(this, R.string.accountUpdatedToast, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}