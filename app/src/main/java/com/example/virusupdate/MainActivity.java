package com.example.virusupdate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
//import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;

public class MainActivity extends FragmentActivity {

    //api to get country wise data
    private static final String virus_update="https://disease.sh/v3/covid-19/countries/India";
    //api to get global data
    private static final String global_update="https://disease.sh/v3/covid-19/all";
    private ProgressBar circle;
    private LinearLayout display;
    /*
    private Spinner spinner;
    Add country names here
    private static final String[] items={};
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display=(LinearLayout) findViewById(R.id.display);
        display.setVisibility(View.GONE);
        circle = (ProgressBar) findViewById(R.id.loading_spinner);
        /*To add items to drop down menu and set on click listener
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //comment the if section when using drop down menu
        if(networkInfo!=null && networkInfo.isConnected()){
            loader task = new loader();
            task.execute(virus_update);
        }
        else{
            circle.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"No internet connection :(",Toast.LENGTH_SHORT).show();
        }
    }
    /*
     @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                call api on the country selected
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
            when nothing is selected call api to get global data
     }
    */

    public void update(virusDetails data){
        display.setVisibility(View.VISIBLE);
        TextView country=(TextView) findViewById(R.id.country);
        country.setText(data.getCountry());
        TextView cases=(TextView) findViewById(R.id.cases);
        cases.setText(data.get_cases());
        TextView recov=(TextView) findViewById(R.id.recovery);
        recov.setText(data.get_recov());
        TextView deaths=(TextView) findViewById(R.id.deaths);
        deaths.setText(data.get_deaths());
    }

    private class loader extends AsyncTask<String,Void,virusDetails> {

        @Override
        protected virusDetails doInBackground(String... strings) {

            virusDetails obj=null;
            String json;
            BufferedReader bf;
            URL url1;
            HttpURLConnection url_con=null;
            InputStream inputStream=null;
            if (strings[0].length() < 1) {
                return null;
            }
            try {
                URL url2=new URL(strings[0]);
                url_con = (HttpURLConnection) url2.openConnection();
                url_con.setRequestMethod("GET");
                url_con.setReadTimeout(10000);
                url_con.setConnectTimeout(15000);
                url_con.connect();
                if (url_con.getResponseCode() == 200) {
                    inputStream = url_con.getInputStream();
                }
                assert inputStream != null;
                InputStreamReader isr=new InputStreamReader(inputStream);
                bf=new BufferedReader(isr);
                StringBuilder builder=new StringBuilder();
                String line=bf.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bf.readLine();
                }
                json=builder.toString();
                JSONObject virus_rep=new JSONObject(json);
                obj = new virusDetails(virus_rep.getString("country"),virus_rep.getString("cases"),virus_rep.getString("todayCases"),virus_rep.getString("deaths"),virus_rep.getString("todayDeaths"),virus_rep.getString("recovered"),virus_rep.getString("todayRecovered"));
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                if (url_con != null) {
                    url_con.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return obj;
        }

        @Override
        protected void onPostExecute(virusDetails virusDetails) {
            circle.setVisibility(View.GONE);
            update(virusDetails);
        }
    }
}