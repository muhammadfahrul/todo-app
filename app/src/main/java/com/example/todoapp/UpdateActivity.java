package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.model.TodoModel;
import com.example.todoapp.network.ServiceGenerator;
import com.example.todoapp.network.response.BaseResponse;
import com.example.todoapp.network.service.TodoService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = UpdateActivity.class.getSimpleName();

    private TextInputEditText etTask;
    private MaterialButton btnUpdate;
    private TodoService service;
    private TodoModel data;

    public static void newInstance(Context context, TodoModel data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG+".data", data);
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        if(getIntent() != null) {
            data = getIntent().getParcelableExtra(TAG+".data");
        }

        initViews();

        service = ServiceGenerator.createBaseService(this, TodoService.class);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = etTask.getText().toString();
                String status = "false";
                Log.e("task", task);
                Log.e("status", status);

                if(isEmpty(task)){
                    etTask.setError("Task must not be empty");
                    etTask.requestFocus();
                }else{
                    updateData(task, status);
                }
            }
        });
    }

    private void updateData(String task, String status) {
        Call<BaseResponse> call = service.apiUpdate(data.getId(), task, status);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(UpdateActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        etTask = (TextInputEditText) findViewById(R.id.etTask);
        btnUpdate = (MaterialButton) findViewById(R.id.btnUpdate);

        etTask.setText(data.getTask());
    }
}