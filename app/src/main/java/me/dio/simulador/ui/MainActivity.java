package me.dio.simulador.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import me.dio.simulador.data.MatchesApi;
import me.dio.simulador.databinding.ActivityMainBinding;
import me.dio.simulador.domain.Match;
import me.dio.simulador.ui.adapter.MatchesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MatchesApi matchesApi;
    private MatchesAdapter matchesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpClient();
        setupMatchList();
        setupMatchListRefresh();
        setupFloatingActionButton();

    }

    private void setupHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cleyson1984.github.io/matches-simulator-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        matchesApi = retrofit.create(MatchesApi.class);
    }


    private void setupMatchList() {
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        findMatchesFromApi();
    }

    private void setupMatchListRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi);
    }
    private void setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener(View -> {
          view.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
         Random random = new Random();
            public void onAnimationEnd(Animator animation){
               for (int i = 0; i < matchesAdapter.getItemCount(); i++) {
          Match match = matchesAdapter.getMatches().get(i);
          Match.getHomeTeam().setScore(random.nextInt(match,getHomeTeam().getStars() + 1));
          Match.getAwayTeam().setScore(random.nextInt(match,getAwayTeam().getStars() + 1));
          matchesAdapter.notifyDataSetChanged(i);

            }
        });
        });

    }

    private void findMatchesFromApi() {
        binding.srlMatches.setRefreshing(true);
        matchesApi.getMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if  (response.isSuccessful()) {
                    List<Match> matches = response.body();
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                }
                binding.srlMatches.setRefreshing(false);
            }


            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                showErrorMessage();
                binding.srlMatches.setRefreshing(false);

            }
        });
    }


    private void showErrorMessage() {
        //Snackbar.make(binding.floatingActionButton,R.string.error, Snackbar.LENGTH_LONG).show();
    }
}}
