package ru.job4j.githubrepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.job4j.githubrepos.Models.GithubRepo;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ReposHolder> {
    private final List<GithubRepo> repos;
    private LayoutInflater inflater;
    private static OnRepoAdapterClickListener callback;


    public ReposAdapter(Context context, List<GithubRepo> repos) {
        this.inflater = LayoutInflater.from(context);
        this.repos = repos;
    }

    @NonNull
    @Override
    public ReposHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_repo, parent, false);

        return new ReposHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReposHolder holder, int i) {
        final GithubRepo repo = repos.get(i);
        holder.textViewName.setText(repo.getName());
        //holder.textViewHours.setText("" + job.getHour());
        //holder.textViewMoney.setText("" + job.getMoney());
        //holder.textViewDesc.setText("" + job.getDesc());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRepoClicked(repo);
            }
        });


    }

    @Override
    public int getItemCount() {
        return repos.size();
    }


    public class ReposHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewHours, textViewMoney, textViewDesc;
        //ImageView imageViewDelete, imageViewDone;
        //CardView cardView;
        View view;

        public ReposHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewItemRepoName);
            //textViewHours = itemView.findViewById(R.id.textViewIjHours);
            //textViewMoney = itemView.findViewById(R.id.textViewIjMoney);
            //textViewDesc = itemView.findViewById(R.id.textViewIjDesc);
            //imageViewDone = itemView.findViewById(R.id.imageViewIjDone);
            //imageViewDelete = itemView.findViewById(R.id.imageViewIjDelete);
            //cardView = itemView.findViewById(R.id.cardViewJob);

            this.view = itemView;
        }


    }

    public void setOnItemClickListener(OnRepoAdapterClickListener clickListener) {
        ReposAdapter.callback = clickListener;
    }

    public interface OnRepoAdapterClickListener {
        public void onRepoClicked(GithubRepo repo);
    }


}