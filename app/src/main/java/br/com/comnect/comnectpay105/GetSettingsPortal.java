package br.com.comnect.comnectpay105;

import static br.com.comnect.comnectpay105.AppDefault.postJSONFromPortal;

import android.os.AsyncTask;
import android.util.Log;

public class GetSettingsPortal {

    private int config = 1;

    public int checkFromWebService(){

        Log.e("ServicePay", "starting check");

        GetJson list = new GetJson();
        list.execute();

        return config;
    }

    private class GetJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(Void... params) {
            Log.e("ServicePay", "doing in background");
            return postJSONFromPortal(routes.getSettingsPortal);
        }

        @Override
        protected void onPostExecute(String list){
            parseConfig(list);
        }
    }

    private void parseConfig(String list){

        try {
            //JSONObject obj = new JSONObject(list);
            //JSONArray ini = obj.getJSONArray("result");

            Log.e("ServicePay", "Lista > "+list);

        } catch (Exception e) {
            Log.e("ServicePay", "erro no log");
            e.printStackTrace();
        }
    }


}

