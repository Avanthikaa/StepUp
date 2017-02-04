package com.revolution.stepup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.content.DialogInterface;

/**
 * Created by Nandash on 05-02-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<People> mDataSet;

    public EventAdapter(Context context, ArrayList<People> people) {
        this.context = context;
        this.mDataSet = people;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uName;
        public Button thanksButton;

        public ViewHolder(View itemView) {
            super(itemView);
            uName = (TextView) itemView.findViewById(R.id.eventUserName);
            thanksButton = (Button) itemView.findViewById(R.id.sayThanks);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.people_list, parent, false);
        BuddyAdapter.ViewHolder viewHolder = new BuddyAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).uName.setText(mDataSet.get(position).uName);
        //((ViewHolder) holder).textView2.setText(mDataSet.get(position).imageUrl);
        final Button fb = ((ViewHolder) holder).thanksButton;

        ((ViewHolder) holder).thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter a Message");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Post tp persons profile here
                        Toast.makeText(context, input.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        } );
    }
}

