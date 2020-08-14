package com.example.todoapp.network.service;

import com.example.todoapp.model.TodoModel;
import com.example.todoapp.network.EndPoint;
import com.example.todoapp.network.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodoService {
    @FormUrlEncoded
    @POST(EndPoint.API_CREATE)
    Call<BaseResponse> apiCreate(
            @Field("task") String task,
            @Field("status") String status
    );

    @GET(EndPoint.API_READ)
    Call<BaseResponse<List<TodoModel>>> apiRead();

    @FormUrlEncoded
    @PUT(EndPoint.API_UPDATE+"{id}")
    Call<BaseResponse> apiUpdate(
            @Path("id") String id,
            @Field("task") String task,
            @Field("status") String status
    );

    @DELETE(EndPoint.API_DELETE+"{id}")
    Call<BaseResponse> apiDelete(@Path("id") String id);
}
