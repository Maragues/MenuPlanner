package com.maragues.menu_planner.ui.planner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;
import com.maragues.menu_planner.ui.common.BaseDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class LabelDialogFragment extends BaseDialogFragment {
  static final String TAG = LabelDialogFragment.class.getSimpleName();

  @BindView(R.id.meal_instance_label_list)
  RecyclerView labelRecyclerView;

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);

  public static LabelDialogFragment newInstance() {
    return new LabelDialogFragment();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = inflateView(LayoutInflater.from(getActivity()), null, R.layout.dialog_meal_instance_label);

    final AlertDialog dialog = new AlertDialog.Builder(getActivity())
            .setTitle(R.string.meal_label_dialog_title)
            .setView(view)
            .create();

    setupLabelList();

    return dialog;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    if (adapter != null)
      adapter.cleanup();
  }

  private void setupLabelList() {
    labelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    labelRecyclerView.setHasFixedSize(true);

    adapter = createAdapter();

    labelRecyclerView.setAdapter(adapter);
  }

  FirebaseRecyclerAdapter adapter;

  private FirebaseRecyclerAdapter createAdapter() {
    isLoadingSubject.onNext(true);

    return new FirebaseRecyclerAdapter<MealInstanceLabel, LabelViewHolder>(
            MealInstanceLabel::create,
            R.layout.item_meal_label,
            LabelViewHolder.class,
            labelQuery()
    ) {
      @Override
      protected void populateViewHolder(LabelViewHolder viewHolder, MealInstanceLabel label, int position) {
        viewHolder.setLabel(label);

        viewHolder.itemView.setOnClickListener(v -> onLabelClicked(label));
      }

      @Override
      public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
      }

      @Override
      public void onDataChanged() {
        super.onDataChanged();

        isLoadingSubject.onNext(false);
      }

      @Override
      public void onCancelled(DatabaseError error) {
        super.onCancelled(error);

        isLoadingSubject.onNext(false);
      }
    };
  }

  private BehaviorSubject<MealInstanceLabel> labelSubject = BehaviorSubject.create();

  Observable<MealInstanceLabel> labelObservable() {
    return labelSubject;
  }

  private void onLabelClicked(MealInstanceLabel label) {
    labelSubject.onNext(label);

    labelSubject.onComplete();

    dismiss();
  }

  @NonNull
  private Query labelQuery() {
    return FirebaseDatabase.getInstance().getReference()
            .child(IMealInstanceLabelProvider.ROOT)
            .orderByChild(IMealInstanceLabelProvider.TIME);
  }

  public static class LabelViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_label_name)
    TextView labelTextView;

    public LabelViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    public void setLabel(MealInstanceLabel label) {
      labelTextView.setText(label.getLocalizedLabelResId());
    }
  }
}
