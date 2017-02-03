package com.maragues.menu_planner.ui.team;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.UserGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 27/1/17.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

  private final List<UserGroup> users;

  private final BehaviorSubject<UserGroup> userClickedSubject = BehaviorSubject.create();

  Observable<UserGroup> userClickedObservable() {
    return userClickedSubject;
  }

  TeamAdapter(List<UserGroup> users) {
    this.users = users;
  }

  @Override
  public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TeamViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_team_member, parent, false));
  }

  @Override
  public void onBindViewHolder(TeamViewHolder holder, int position) {
    holder.render(users.get(position));

    holder.itemView.setOnClickListener(ignored -> userClickedSubject.onNext(users.get(position)));
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  static class TeamViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.team_member_name)
    TextView nameTextView;

    @BindView(R.id.team_member_role)
    TextView roleTextView;

    @BindView(R.id.team_member_thumbnail)
    ImageView thumbnailImageView;

    public TeamViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    public void render(@NonNull UserGroup userGroup) {
      nameTextView.setText(userGroup.name());

      roleTextView.setText(userGroup.role());
    }
  }
}
