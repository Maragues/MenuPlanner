package com.maragues.menu_planner.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.maragues.menu_planner.R;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by maragues on 05/12/2016.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<DisplayableViewHolder> {
  final List<Displayable> items = new ArrayList<>();

  public MenuRecyclerViewAdapter(List< ? extends Displayable> displayables){
    items.addAll(displayables);
  }

  @Override
  public DisplayableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DisplayableViewHolder(
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_menu, parent, false)
    );
  }

  @Override
  public void onBindViewHolder(DisplayableViewHolder holder, int position) {
    holder.setDisplayable(
            items.get(position),
            position == 0 ? null : items.get(position - 1)
    );
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public interface Displayable {
    @NonNull
    LocalDate date();

    @NonNull
    String title();

    @NonNull
    List<String> recipes();
  }
}
