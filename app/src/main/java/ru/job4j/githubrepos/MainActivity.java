package ru.job4j.githubrepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.job4j.githubrepos.Models.GithubRepo;
import ru.job4j.githubrepos.Models.User;

public class MainActivity extends AppCompatActivity {

    private TextView textViewUserName, textViewUserEmail;
    private ImageView imageViewUserPict, imageViewLogo;
    private EditText editTextLogin, editTextPass;
    private Button buttonLogin;
    private NetworkService networkService = NetworkService.getInstance();
    //private final static String credentials = Credentials.basic("dmk78", "Ek193burg");
    private RecyclerView recycler;
    private ReposAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserUserEmail);
        imageViewUserPict = findViewById(R.id.imageViewUserPicture);
        imageViewLogo = findViewById(R.id.imageView);


        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPass = findViewById(R.id.editTextPass);
        buttonLogin = findViewById(R.id.buttonLogin);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextLogin.getText().toString().isEmpty()){
                    editTextLogin.setError("Enter username");
                    editTextLogin.requestFocus();
                } else if(editTextPass.getText().toString().isEmpty()){
                    editTextPass.setError("Enter password");
                    editTextPass.requestFocus();
                } else {
                    String credentials = Credentials.basic(editTextLogin.getText().toString(), editTextPass.getText().toString());
                    getCurrentUser(credentials);
                }
            }
        });
        this.recycler = findViewById(R.id.recyclerRepos);
        this.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    private void getCurrentUser(final String credentials) {
        networkService.getJSONApi().getCurrentUser(credentials).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    User user = response.body();
                    textViewUserName.setText(user.getName().toString());
                    textViewUserEmail.setText(user.getEmail().toString());
                    String avatar = user.getAvatarUrl();
                    if (avatar != null && !avatar.equals("null") && !avatar.equals("")) {
                        Picasso.with(getApplicationContext()).load(user.getAvatarUrl())
                                .into(imageViewUserPict);
                    }


                    getAuthUserRepos(credentials);

                }
                Toast.makeText(MainActivity.this, "Error connect", Toast.LENGTH_SHORT).show();
                Log.i("MyError", "" + response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("MyError", "" + t.getMessage());
            }
        });
    }


    private void getAuthUserRepos(String s) {
        networkService.getJSONApi().listUserRepos(s).enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                if (response.isSuccessful()) {

                    List<GithubRepo> repos = response.body();

                    adapter = new ReposAdapter(getApplicationContext(), repos);
                    adapter.setOnItemClickListener(new ReposAdapter.OnRepoAdapterClickListener() {
                        @Override
                        public void onRepoClicked(GithubRepo repo) {
                            Toast.makeText(MainActivity.this, "" + repo.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    recycler.setAdapter(adapter);
                }
                Log.i("MyError", "" + response.code());
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Log.i("MyError", "" + t.getMessage());
            }
        });
    }

    private void getReposforUser(String username) {
        networkService.getJSONApi().listRepos(username).enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                if (response.isSuccessful()) {

                    List<GithubRepo> repos = response.body();
                    for (GithubRepo repo : repos) {
                        textViewUserName.append(repo.getName() + "\n");
                    }
                }
                Log.i("MyError", "" + response.code());
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Log.i("MyError", "" + t.getMessage());
            }
        });
    }
}
