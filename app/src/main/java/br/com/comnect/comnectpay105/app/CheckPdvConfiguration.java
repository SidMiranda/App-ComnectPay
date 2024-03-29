package br.com.comnect.comnectpay105.app;

import static br.com.comnect.comnectpay105.app.AppDefault.getJSONFromAPI;

import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckPdvConfiguration {

    private int config = 1;

    public int checkFromWebService(){

        GetJson list = new GetJson();
        list.execute();

        return config;
    }

    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI(routes.getPdvConfig + Build.SERIAL);
        }

        @Override
        protected void onPostExecute(String list){
            parseConfig(list);
        }
    }

    private void parseConfig(String list){

        try {
            JSONObject obj = new JSONObject(list);
            JSONArray ini = obj.getJSONArray("result");

            if(ini != null){
                config = 1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
