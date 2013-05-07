package com.example.morningreport;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	final List<String> TITLES = new ArrayList<String>();
	protected FeedsDataSource fds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Context context = this;
		fds = new FeedsDataSource(context);
		fds.open();
		// remove clear line before deploy - only un comment when adding new
		// things to db
		fds.clear();
		// if feeds table contains anything
		if (fds.getAllFeeds().size() == 0) {
			fds.createRSS("PC World",
					"http://feeds.pcworld.com/pcworld/latestnews");
			fds.createRSS("ESPN Top", "http://sports.espn.go.com/espn/rss/news");
			fds.createRSS("BBC World",
					"http://feeds.bbci.co.uk/news/world/rss.xml");
			fds.createRSS("CNN Top", "http://rss.cnn.com/rss/edition.rss");
		}

		for (String s : fds.getRssMap().keySet()) {
			TITLES.add(s);
		}
		TITLES.add("Reddit");

		new GetRSSFeedTask(this).execute(fds.getAllFeedsArray());

		setContentView(R.layout.activity_main);
		final ListView listView = (ListView) findViewById(R.id.mainListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, TITLES);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long myLong) {
				String selected = (String) (listView
						.getItemAtPosition(myItemInt));
				Intent intent = new Intent(context, RSSActivity.class);

				if (selected.equals("Reddit")) {
					intent = new Intent(context, RedditActivity.class);
				} else {
					intent.putExtra("URL", fds.getUrlForTitle(selected));
					intent.putExtra("TITLE", selected);
				}
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

	public boolean onOptionsItemSelected(MenuItem item) {
		// respond to menu item selection
		switch (item.getItemId()) {
		case R.menu.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		case R.menu.help:
			startActivity(new Intent(this, HelpActivity.class));
			return true;
		case R.menu.contact:
			startActivity(new Intent(this, ContactActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class GetRSSFeedTask extends AsyncTask<Feed, Void, List<Item>> {

		private Context context;
		private ItemsDataSource ids;

		public GetRSSFeedTask(Context c) {
			this.context = c;
		}

		@Override
		protected List<Item> doInBackground(Feed... feeds) {
			String title = "";
			String url = "";
			String desc = "";
			String feedTitle = "";

			URL feedUrl;
			List<Item> items = new ArrayList<Item>();
			ids = new ItemsDataSource(context);
			ids.open();
			ids.clear();
			for (Feed f : feeds) {
				try {
					feedUrl = new URL(f.getUrl());
					feedTitle = f.getTitle();
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(false);
					XmlPullParser xpp = factory.newPullParser();

					xpp.setInput(getInputStream(feedUrl), "UTF_8");

					boolean insideItem = false;

					int eventType = xpp.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {

						if (eventType == XmlPullParser.START_TAG) {
							if (xpp.getName().equalsIgnoreCase("item")) {
								insideItem = true;
							} else if (xpp.getName().equalsIgnoreCase("title")) {
								// getting the headline
								if (insideItem) {
									title = xpp.nextText();
								}
							} else if (xpp.getName().equalsIgnoreCase("link")) {
								// getting the title
								if (insideItem) {
									url = xpp.nextText();
								}
							} else if (xpp.getName().equalsIgnoreCase(
									"description")) {
								// getting the description
								if (insideItem) {
									desc = xpp.nextText();
								}
							}
						} else if (eventType == XmlPullParser.END_TAG
								&& xpp.getName().equalsIgnoreCase("item")) {
							if (title != null && url != null && desc != null) {
								items.add(ids.createItem(title, url, desc,
										feedTitle));
							}
							title = null;
							url = null;
							desc = null;
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
			}

			return items;
		}

		@Override
		protected void onPostExecute(List<Item> itemList) {
			super.onPostExecute(itemList);
			System.out.println("DB IDS " + ids.getAllItems().size());
			for (Item i : ids.getAllItems()) {
				System.out.println("DB : " + i.getTitle());
			}
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
