package com.example.aaquibkhwaja.hackday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends Activity {
    class autoComplete<String> extends ArrayAdapter<String> implements Filterable {

        public autoComplete(Context context, int resource) {
            super(context, resource);
        }

        public Filter getFilter() {

            return new Filter()
            {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence)
                {
                    FilterResults results = new FilterResults();

                    //If there's nothing to filter on, return the original data for your list
                    ArrayList<String> filterResultsData = new ArrayList<String>();


                    results.values = lanResult;
                    results.count = size;

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                {
                    filteredData = (java.lang.String[]) filterResults.values;
                    notifyDataSetChanged();
                }
            };    }


    }

    String lanResult[] = new String[5];
    String filteredData[] = new String[5];
    int size;
    AutoCompleteTextView editText;
    String finalResult;
    boolean looping = false;
    String finalTextString = "";
    boolean flag[] = new boolean[100];
    int cnt = 0;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnSpinnerItemSelection();

        editText = (AutoCompleteTextView)findViewById(R.id.editText);
        final String language = spinner.getSelectedItem().toString();


        Button button = (Button)findViewById(R.id.button);
        final Helper h = new Helper();

        final autoComplete<String> adapter = new autoComplete<>(this,android.R.layout.simple_dropdown_item_1line);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().length() < finalTextString.length()) {
                        flag[--cnt] = false;
                        String arr[] = finalTextString.split(" ");
                        finalTextString = "";
                        for (int i = 0; i < arr.length - 1; i++) {
                            finalTextString = finalTextString + arr[i] + " ";
                        }
                    }
                    if (s.toString().length() == 0) {
                        System.out.println("yes");
                        return;
                    }
                    if (s.charAt(s.length() - 1) == ' ') {
                        System.out.println("final = " + finalTextString + "len = " + (finalTextString.split(" ").length - 1) + " flag = " + flag[finalTextString.split(" ").length - 1]);
                        if (!flag[s.toString().split(" ").length - 1]) {
                            System.out.println("Entered here ");
                            String input = s.toString();
                            String arr[] = input.split(" ");
                            String last = arr[arr.length - 1];
                            System.out.println("last = " + last);
                            String language1 = spinner.getSelectedItem().toString();
                            lanResult = h.inputToolOutput(last, language1);
                            System.out.println("len = " + arr.length + " lanRes = " + lanResult[0]);
                            arr[arr.length - 1] = lanResult[0];
                            finalTextString = "";
                            for (String a : arr) {
                                System.out.println("a = " + a);
                                finalTextString = finalTextString + a + " ";
                            }
                            System.out.println("fibal = " + finalTextString);
                            System.out.println("Error here0");
                            flag[cnt++] = true;
                            editText.setText(finalTextString);
                            System.out.println("Error here1");
                            editText.setSelection(finalTextString.length());
                            System.out.println("Error here2");
                        } else {
                        }
                    } else {
                        adapter.clear();
                        System.out.println("s = " + s.toString());
                        String input = s.toString();
                        String arr[] = input.split(" ");
                        String last = arr[arr.length - 1];
                        String language1 = spinner.getSelectedItem().toString();
                        lanResult = h.inputToolOutput(last, language1);
                        System.out.println("final = " + finalTextString);
                        for (String a : lanResult) {
                            String toadd = finalTextString + a;
                            System.out.println("toadd = " + toadd);
                            adapter.add(toadd);
                        }

                        adapter.notifyDataSetChanged();
                        System.out.println("adapter = " + adapter);
                        editText.setAdapter(adapter);
                        editText.showDropDown();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = editText.getText().toString();
                String language1 = spinner.getSelectedItem().toString();

                String query = h.getResult(input, language1);

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
        ArrayList<String> array = new ArrayList<String>();
        array.add("English");
        array.add("हिन्दी");
        array.add("ಕನ್ನಡ");
        array.add("বাঙালি");
        array.add("মালায়ালম");
        array.add("தமிழ்");
        array.add("తెలుగు");
        array.add("मराठी");
        array.add("español");
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, array);
        spinner.setAdapter(mAdapter);
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
