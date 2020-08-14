package com.example.todoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.adapter.TodoAdapter;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.listener.OnDeleteClickListener;
import com.example.todoapp.listener.OnUpdateClickListener;
import com.example.todoapp.model.TodoModel;
import com.example.todoapp.network.ServiceGenerator;
import com.example.todoapp.network.response.BaseResponse;
import com.example.todoapp.network.service.TodoService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements OnDeleteClickListener, OnUpdateClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvTask;
    private TodoAdapter adapter;
    private TodoService service;
    TextInputEditText etNewTask;
    MaterialButton btnCreate;
    ProgressBar pbLoading;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        // Initialization adapter
        adapter = new TodoAdapter(this);
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        service = ServiceGenerator.createBaseService(this, TodoService.class);

        rvTask.setAdapter(adapter);
        loadData();

//        showLoading(true);
    }

    private void loadData() {
        Call<BaseResponse<List<TodoModel>>> call = service.apiRead();
        call.enqueue(new Callback<BaseResponse<List<TodoModel>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<TodoModel>>> call, Response<BaseResponse<List<TodoModel>>> response) {
                if(response.code() == 200) {
                    adapter.addAll(response.body().getData());
                    initListener();
                }

//                showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<List<TodoModel>>> call, Throwable t) {
                Log.e(TAG+".error", t.toString());

//                showLoading(false);
            }
        });
    }

    private void initListener() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = etNewTask.getText().toString();
                String status = "false";
                Log.e("task", task);
                Log.e("status", status);

                if(isEmpty(task)){
                    etNewTask.setError("Task must not be empty");
                    etNewTask.requestFocus();
                }else{
                    addData(task, status);
                }
            }
        });
        adapter.setOnDeleteClickListener(this);
        adapter.setOnUpdateClickListener(this);
    }

    private void addData(String task, String status) {
        Call<BaseResponse> call = service.apiCreate(task, status);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    etNewTask.setText("");
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        rvTask = (RecyclerView) findViewById(R.id.rvTask);
        etNewTask = (TextInputEditText) findViewById(R.id.etNewTask);
        btnCreate = (MaterialButton) findViewById(R.id.btnCreate);
    }

    private void doDelete(final int position, String id) {
        Call<BaseResponse> call = service.apiDelete(id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200)
                    adapter.remove(position);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG+".errorDelete", t.toString());
            }
        });
    }

    @Override
    public void onDeleteClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                doDelete(position, adapter.getData(position).getId());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onUpdateClick(int position) {
        TodoModel data = adapter.getData(position);
        UpdateActivity.newInstance(this, data);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            pbLoading.setVisibility(View.VISIBLE);
            rvTask.setVisibility(View.GONE);
            etNewTask.setVisibility(View.VISIBLE);
            btnCreate.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.GONE);
            rvTask.setVisibility(View.VISIBLE);
            etNewTask.setVisibility(View.VISIBLE);
            btnCreate.setVisibility(View.VISIBLE);
        }
    }
}