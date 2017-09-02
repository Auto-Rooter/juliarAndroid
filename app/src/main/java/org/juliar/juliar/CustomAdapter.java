package org.juliar.juliar;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Andrey Makhanov on 12/4/2016.
    */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<MyData> my_data;
    public EditText inter_text;

    public CustomAdapter(Context context, List<MyData> my_data,EditText inter_text) {
        this.context = context;
        this.my_data = my_data;
        this.inter_text = inter_text;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.currdate.setText(my_data.get(position).getCurrdate());
        holder.output.setText(my_data.get(position).getOutput());
        holder.original.setText(my_data.get(position).getOriginal());

        holder.original.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                inter_text.setText(my_data.get(position).getOriginal());
            }
        });

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView currdate;
        public TextView output;
        public TextView original;

        public ViewHolder(View itemView) {
            super(itemView);
            currdate = (TextView) itemView.findViewById(R.id.currdate);
            output = (TextView) itemView.findViewById(R.id.output);
            original = (TextView) itemView.findViewById(R.id.original);

            original.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        }
    }
}
