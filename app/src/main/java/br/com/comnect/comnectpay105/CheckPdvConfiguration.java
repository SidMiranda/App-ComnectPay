package br.com.comnect.comnectpay105;

import static br.com.comnect.comnectpay105.AppDefault.getJSONFromAPI;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckPdvConfiguration {

    private static String Model;
    private int config = 0;

    public CheckPdvConfiguration(String Model){
        this.Model = Model;
    }

    public int getConfig(){
        return checkFromWebService(Model);
    }

    public int checkFromWebService(String Model){

        GetJson list = new GetJson();
        list.execute();

        return config;
    }

    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(Void... params) {
            return getJSONFromAPI(routes.getPdvConfig + Model);
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
