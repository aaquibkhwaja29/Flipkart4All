package com.example.aaquibkhwaja.hackday;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnSpinnerItemSelection();

        final EditText editText = (EditText)findViewById(R.id.editText);

        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = editText.getText().toString();
                String language = spinner.getSelectedItem().toString();

                Helper h = new Helper();
                String query = h.getResult(input, language);

                System.out.println("query " + query);

                //final String query = editText.getText().toString();

                query.replace(" ", "+");

                Uri uri = Uri.parse("http://dl.flipkart.com/dl/search?q=" + query + "&query=" + query + "&sid=all");

                Intent launchIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(launchIntent);
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            System.out.println("ITEM SELECTED = " + parent.getItemAtPosition(pos).toString());
// Toast.makeText(parent.getContext(),
// "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
// Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
        }

    }
}
