package com.davozebi.termproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<PassModel> localDataSet;

    private LayoutInflater inflater;
    private static ItemClickListener clickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvService;
        private final TextView tvUserName;

        private final TextInputEditText etPass;

        private final Button btnCopyPass;
        private final Button btnEdit;
        private final Button btnRemove;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            tvService = view.findViewById(R.id.tv_services);
            tvUserName = view.findViewById(R.id.tv_user_name);
            etPass = view.findViewById(R.id.et_pass);

            btnCopyPass = view.findViewById(R.id.btn_copy_pass);
            btnEdit = view.findViewById(R.id.btn_edit);
            btnRemove = view.findViewById(R.id.btn_remove);

            btnCopyPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        public TextView getTvService() {
            return tvService;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public TextInputEditText getEtPass() {
            return etPass;
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MyRecyclerViewAdapter(Context context, List<PassModel> dataSet) {
        inflater = LayoutInflater.from(context);
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = inflater
                .inflate(R.layout.rv_services_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTvService().setText(localDataSet.get(position).getService());
        viewHolder.getTvUserName().setText(localDataSet.get(position).getUserName());
        viewHolder.getEtPass().setText(localDataSet.get(position).getPass());
    }

    PassModel getItem(int id) {
        return localDataSet.get(id);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}