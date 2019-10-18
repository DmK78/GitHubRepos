package ru.job4j.githubrepos;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

import ru.job4j.githubrepos.Models.GithubIssue;
import ru.job4j.githubrepos.Models.GithubRepo;
import ru.job4j.githubrepos.Models.User;

public interface JsonPlaceHolderApi {
    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> listRepos(@Path("user") String user);

    @GET("/user/repos")
    Call<List<GithubRepo>> listUserRepos(@Header("Authorization") String credentials);

    @GET("/user")
    Call<User> getCurrentUser(@Header("Authorization") String credentials);

}
