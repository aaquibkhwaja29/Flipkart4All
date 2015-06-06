package com.example.aaquibkhwaja.hackday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaquib.khwaja on 06/06/15.
 */
public class Helper {
    EditText MyInputText;
    Button MyTranslateButton;
    TextView MyOutputText;
    Map <String, String> LanMap = new HashMap<String, String>(){{
       put("English", "en");
        put("हिन्दी", "hi");
        put("ಕನ್ನಡ", "kn");
        put("বাঙালি", "bn");
        put("মালায়ালম", "ml");
        put("தமிழ்", "ta");
        put("తెలుగు", "te");
        put("मराठी", "mr");
        put("español", "es");
    }};

    String finalResult;
    boolean looping = false;
    /** Called when the activity is first created. */
    public String getResult(String InputString, String lan) {
        // TODO Auto-generated method stub
        String LanCode = LanMap.get(lan);
        String OutputString = null;
        System.out.println("Input = " + InputString);
        try {

//                https://translate.google.com/translate_a/single?client=z&sl=en&tl=hi-CN&ie=UTF-8&oe=UTF-8&dt=t&dt=rm&q=red%20shirt
//               https://inputtools.google.com/request?text=dheeru bhai is the man&itc=hi-t-i0-und&num=10&cp=0&cs=1&ie=utf-8&oe=utf-8&app=test
       //     String LanguageOutput = inputToolOutput(InputString, LanCode);
            OutputString = translate(InputString, LanCode);
        } catch (Exception ex) {
            System.out.println("Exception = " + ex.toString());
            ex.printStackTrace();
            OutputString = "Error";
        }
        return OutputString;
    }

    public String[] inputToolOutput(String input, String lan) throws JSONException {
        String input1 = input.replaceAll(" ", "%20");
        System.out.println("input to inputTool = " + input);
        lan = LanMap.get(lan);
        StringBuilder url2 = new StringBuilder("https://inputtools.google.com/request?");
        url2.append("text=" + input1);
        url2.append("&itc=" + lan + "-t-i0-und");
        url2.append("&num=10");
        url2.append("&cp=0");
        url2.append("&cs=1");
        url2.append("&ie=utf-8");
        url2.append("&oe=utf-8");
        url2.append("&app=test");
        looping = true;
        boolean called = false;
        while (looping) {
            if (!called) {
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(url2.toString());
                called = true;
            }
        }
        String OutputString = finalResult;

        JSONArray JO = new JSONArray(OutputString);
        System.out.println("JO = " + JO);
        System.out.println("JO[1] = " + JO.get(1));
        JSONArray JO1 = new JSONArray(JO.get(1).toString());
        System.out.println("JO1 = " + JO1);
        System.out.println("JO[1][1] = " + new JSONArray(JO1.get(0).toString()).get(1));

        JSONArray Output = new JSONArray(new JSONArray(JO1.get(0).toString()).get(1).toString());
        String result[] = new String[Output.length()];
        int size;
        size = Output.length();
        for (int i = 0; i < Output.length() && i < 5; i++) {
            System.out.println("Output(" + i + ") = " + Output.get(i).toString());
            result[i] = Output.get(i).toString();
            if (i == 4) {
                size = 4;
            }
        }

        System.out.println("result = " + result.toString());

        return result;
    }


    public String translate(String input, String LanCode) {
        String input1 = input.replaceAll(" ", "%20");
        System.out.println("input to Translate = " + input);
        StringBuilder url1 = new StringBuilder("https://translate.google.com/translate_a/single?");
        url1.append("client=z");
        url1.append("&sl=" + LanCode);
        url1.append("&tl=en-CN");
        url1.append("&ie=UTF-8");
        url1.append("&oe=UTF-8");
        url1.append("&dt=t");
        url1.append("&dt=rm");
        url1.append("&q=" + input1);
        looping = true;
        boolean called = false;
        while (looping) {
            if (!called) {
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(url1.toString());
                called = true;
            }
        }
        String OutputString = finalResult.split("\"")[1];
        System.out.println("Output = " + OutputString + " " + finalResult);
        return OutputString;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            System.out.println("downloadUrl url = " + url);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("downloadUrl urlConnection = " + urlConnection);
            // Connecting to url
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            int responseCode = urlConnection.getResponseCode();
//            urlConnection.connect();
            System.out.println("downloadUrl urlConnection = " + urlConnection);
            // Reading data from url
            iStream = urlConnection.getInputStream();
            System.out.println("downloadUrl iStream = " + iStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            System.out.println("downloadUrl sb = " + sb);
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            System.out.println("downloadUrl sb = " + sb);
            data = sb.toString();
            br.close();
        }catch(Exception e){
//            Log.d("Exception while downloading url", e.toString());
            System.out.println("Exception = " + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        finalResult = data;
        looping = false;
        System.out.println("data = " + data);
        return data;
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = null;
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
                System.out.println(data + " = data");
            }catch(Exception e){
                System.out.println("Background Task " + e.toString());
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            System.out.println(result + " = result");
            //        ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            //         parserTask.execute(result);
        }
    }
}
