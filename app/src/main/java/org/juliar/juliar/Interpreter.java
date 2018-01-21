package org.juliar.juliar;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsnag.android.Bugsnag;
import com.juliar.JuliarLib;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

public class Interpreter extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<MyData> data_list;

    public ScaleGestureDetector scaleGestureDetector;
    public float mScaleFactor = 1.f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bugsnag.init(this);

        Bugsnag.notify(new RuntimeException("Non-fatal"));

        setContentView(R.layout.activity_interpreter);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();

        //Some Function
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("h:mm:ss").format(new Date());

        MyData data = new MyData(timeStamp,"Welcome to Juliar (Android version) created by Juliar Team. Version: 2018",
                "Juliar Welcome Message");
        data_list.add(data);
        //

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        EditText inter_text = (EditText) findViewById(R.id.input_text);

        adapter = new CustomAdapter(this,data_list,inter_text);
        recyclerView.setAdapter(adapter);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        //Add Delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast toast = Toast.makeText(getApplicationContext(), "Deleted",Toast.LENGTH_SHORT);
                toast.show();
                //Remove swiped item from list and notify the RecyclerView
                data_list.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        onExecClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        Log.d("Event","MYEVENT");
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }


    public class ScaleListener extends SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            EditText inter_text = (EditText) findViewById(R.id.input_text);
            inter_text.setText("Hello");
            float size = ((TextView)findViewById(R.id.output)).getTextSize();
            Log.d("TextSizeStart", String.valueOf(size));

            float factor = detector.getScaleFactor();
            Log.d("Factor", String.valueOf(factor));


            float product = size*factor;
            Log.d("TextSize", String.valueOf(product));
            ((TextView)findViewById(R.id.output)).setTextSize(TypedValue.COMPLEX_UNIT_PX, product);

            size = ((TextView)findViewById(R.id.output)).getTextSize();
            Log.d("TextSizeEnd", String.valueOf(size));
            return true;
        }
    }

    public void onExecClick()
    {
        Button btnExec = (Button)findViewById(R.id.exec_btn);
        btnExec.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg){
                EditText inter_text = (EditText) findViewById(R.id.input_text);
                String currentcmd = inter_text.getText().toString().trim();
                if(currentcmd.matches("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please input command!",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String timeStamp = new SimpleDateFormat("h:mm:ss").format(new Date());
                    MyData data = new MyData(timeStamp,androidInterpret(currentcmd),currentcmd);
                    data_list.add(data);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    inter_text.setText("");
                }
            }
        });
    }

    public String androidInterpret(String msg) {
        try {
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            ByteArrayOutputStream newErr = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(newOut);
            PrintStream ps2 = new PrintStream(newErr);
            // IMPORTANT: Save the old System.out!
            // Tell Java to use your special stream
            setOut(ps);
            setErr(ps2);
            JuliarAndroid interpreter = new JuliarAndroid();
            interpreter.compile(msg);
            out.flush();
            //setOut(out);
            //setErr(err);
            return newOut.toString() + newErr.toString();
        } catch(Exception e) {
            return e.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interpreter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
