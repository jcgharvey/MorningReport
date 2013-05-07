package com.example.morningreport;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.morningreport.cardcreators.CardCreator;
import com.example.morningreport.cardcreators.RSSCardOnClickListener;
import com.example.morningreport.cardcreators.RSSItemCardCreator;

public class RSSActivity extends Activity {
	
	private String _provider;
	private View _contextMenuSpawner;
	

	public void print(String s) {
		System.out.println(s);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss);
		// initializing variables
		print("Making new RSS Activity");
	
		Intent intent = getIntent();
		String url = intent.getStringExtra("URL");
		String title = intent.getStringExtra("TITLE");
		this.setTitle(title);
		this._provider = title;
		
		new GetRSSFeedTask(this).execute(url);
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onReceiveRSSData(Map<String, String> map) {
		// Loop over every k, v pair and make a new card for it.
		print("map received");
		
		Set<String> postTitles = map.keySet();
		print(" map has " +postTitles.size() + "items");
		for (String postTitle : postTitles) {
			CardCreator cardCreator = new RSSItemCardCreator(this._provider, R.drawable.rss_logo, postTitle, map.get(postTitle));
			print(cardCreator.toString());
			createCard(cardCreator);
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		this._contextMenuSpawner = v;
		
		menu.add(menu.NONE, R.id.action_delete_card, menu.NONE, "Delete");
	}
 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_delete_card){
			Toast.makeText(this, "AWWWW YEAH DELETE ME SON", Toast.LENGTH_LONG).show();
			
			LinearLayout cardContainer = (LinearLayout) findViewById(R.id.cardContainer);
			cardContainer.removeView(this._contextMenuSpawner);
			
			this._contextMenuSpawner = null;
			
		}
		return true;
	}
	
	private void createCard(CardCreator cardCreator) {
		View card = cardCreator.createView(this);
		card.setOnClickListener(new RSSCardOnClickListener(this, cardCreator.getUrl()));
		card.setOnLongClickListener(new RSSCardOnLongClickListener());
		
		registerForContextMenu(card);
		
		LinearLayout cardContainer = (LinearLayout) findViewById(R.id.cardContainer);
		cardContainer.addView(card);
		
	}

	private class GetRSSFeedTask extends
			AsyncTask<String, Void, Map<String, String>> {

		private Context context;

		public GetRSSFeedTask(Context c) {
			this.context = c;
			((RSSActivity) context).print("Creating async task");
		}

		@Override
		protected Map<String, String> doInBackground(String... urls) {
			String headline = "";
			String link = "";
			Map<String, String> rssMap = new HashMap<String, String>();
			try {
				URL url = new URL(urls[0]);
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser xpp = factory.newPullParser();

				xpp.setInput(getInputStream(url), "UTF_8");

				boolean insideItem = false;

				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if (xpp.getName().equalsIgnoreCase("item")) {
							insideItem = true;
						} else if (xpp.getName().equalsIgnoreCase("title")) {
							// getting the headline
							if (insideItem) {
								headline = xpp.nextText();
							}
						} else if (xpp.getName().equalsIgnoreCase("link")) {
							// getting the title
							if (insideItem) {
								link = xpp.nextText();
							}
						}
					} else if (eventType == XmlPullParser.END_TAG
							&& xpp.getName().equalsIgnoreCase("item")) {
						if (headline != null && link != null)
							rssMap.put(headline, link);
						headline = null;
						link = null;
						insideItem = false;
					}
					eventType = xpp.next();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return rssMap;
		}
		
		@Override
		protected void onPostExecute(Map<String, String> map) {
			super.onPostExecute(map);
			((RSSActivity) context).print("Finished async task");
			((RSSActivity) context).onReceiveRSSData(map);
		}

		private InputStream getInputStream(URL url) {
			try {
				return url.openConnection().getInputStream();
			} catch (IOException e) {
				return null;
			}

		}
	}
	
}
