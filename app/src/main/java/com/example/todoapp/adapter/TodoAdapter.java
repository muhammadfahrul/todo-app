package com.example.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.databinding.TaskItemBinding;
import com.example.todoapp.listener.OnDeleteClickListener;
import com.example.todoapp.listener.OnUpdateClickListener;
import com.example.todoapp.model.TodoModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    Context context;
    List<TodoModel> data;

    OnDeleteClickListener onDeleteClickListener;
    OnUpdateClickListener onUpdateClickListener;

    public TodoAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void add(TodoModel item) {
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<TodoModel> items) {
        for (TodoModel item : items) {
            add(item);
        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public TodoModel getData(int position) {
        return data.get(position);
    }

    public void remove(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvTask;
        MaterialTextView tvUpdatedAt;
        MaterialButton btnUpdate;
        MaterialButton btnDelete;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false));
            initViews();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUpdateClickListener.onUpdateClick(getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void bind(TodoModel item) {
            tvTask.setText(item.getTask());
            tvUpdatedAt.setText(item.getUpdatedAt());
        }

        public void initViews() {
            tvTask = (MaterialTextView) itemView.findViewById(R.id.tvTask);
            tvUpdatedAt = (MaterialTextView) itemView.findViewById(R.id.tvUpdatedAt);
            btnUpdate = (MaterialButton) itemView.findViewById(R.id.btnUpdate);
            btnDelete = (MaterialButton) itemView.findViewById(R.id.btnDelete);
        }
    }
}
