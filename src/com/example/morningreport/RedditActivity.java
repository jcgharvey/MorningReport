package com.example.morningreport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RedditActivity extends ListActivity {

	private ArrayAdapter _adapter;
	private List<String> _ids = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reddit);
		
		
		final String subredditName = "pics";
		new GetRedditData().execute("http://www.reddit.com/r/"+subredditName+".json");
		
		((ListView) findViewById(android.R.id.list)).setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				
				Uri uri = Uri.parse((String) "http://www.reddit.com/r/" + subredditName + "/comments/" + _ids.get(pos));
				System.out.println(uri.toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class GetRedditData extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {
		
		private Context _context;
		
		public GetRedditData () {
			_context = RedditActivity.this;
		}

		@Override
		protected ArrayList<ArrayList<String>> doInBackground(String... urls) {
			JSONObject data = getJSONfromURL(urls[0]);
			
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();;
			
			ArrayList<String> titles = new ArrayList<String>();
			ArrayList<String> ids = new ArrayList<String>();
			try {
				JSONArray posts = data.getJSONObject("data").getJSONArray("children");
				int numPosts = posts.length();
				
				for (int i = 0 ; i < numPosts ; i++) {
					String title = posts.getJSONObject(i).getJSONObject("data").getString("title");
					String id = posts.getJSONObject(i).getJSONObject("data").getString("name").substring(3);
					titles.add(title);
					ids.add(id);
				}
				
				result.add(titles);
				result.add(ids);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = null;
				System.out.println("GOT OUT");
			}
			
			
			return result;
			
		}
		
		public JSONObject getJSONfromURL(String url) { 
			//initialize
			InputStream is = null;
			String result = "";
			JSONObject jArray = null;
			
			System.out.println(url);
			 
			//http post
			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httppost = new HttpGet(url);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("log_tag", "Error in http connection "+e.toString());
			}
			 
			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}
			System.out.println(result);
			//try parse the string to a JSON object
			try{
				jArray = new JSONObject(result);
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			return jArray;
		}
		
		protected void onPostExecute(ArrayList<ArrayList<String>> result){
			super.onPostExecute(result);
			
			System.out.println("after executing");
			
			((RedditActivity) this._context)._ids = result.get(1);
			
			_adapter = new ArrayAdapter<String>(this._context,android.R.layout.simple_list_item_1, result.get(0));
			//this.context.links = result.get(1);
			setListAdapter(_adapter);
			
			
		}
		
	}

}
