package com.example.todoapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TodoModel implements Parcelable{

	@SerializedName("id")
	String id;
	@SerializedName("task")
	String task;
	@SerializedName("status")
	String status;
	@SerializedName("createdAt")
	String createdAt;
	@SerializedName("updatedAt")
	String updatedAt;

	protected TodoModel(Parcel in) {
		id = in.readString();
		task = in.readString();
		status = in.readString();
		createdAt = in.readString();
		updatedAt = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(task);
		dest.writeString(status);
		dest.writeString(createdAt);
		dest.writeString(updatedAt);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<TodoModel> CREATOR = new Parcelable.Creator<TodoModel>() {
		@Override
		public TodoModel createFromParcel(Parcel in) {
			return new TodoModel(in);
		}

		@Override
		public TodoModel[] newArray(int size) {
			return new TodoModel[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}