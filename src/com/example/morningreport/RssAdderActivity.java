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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RssAdderActivity extends Activity {
	Button addButton;
	EditText addURL;
	EditText addTitle;
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_adder);

		addButton = (Button) findViewById(R.id.addButton);
		addURL = (EditText) findViewById(R.id.feedUrl);
		addTitle = (EditText) findViewById(R.id.feedTitle);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String url = addURL.getText().toString();
				String title = addTitle.getText().toString();
				
				if (url == "" || url == null || title == "" || title == null ) {
					// bad
				} else {
					// try to load
					Feed f = new Feed(title, url);
					new GetRSSFeedTask(context).execute(f);
					// if pass load - add to database

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rss_adder, menu);
		return true;
	}

	private class GetRSSFeedTask extends AsyncTask<Feed, Void, List<Item>> {

		private Context context;
		private ItemsDataSource ids;
		private FeedsDataSource fds;

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
			fds = new FeedsDataSource(context);
			fds.open();
			
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
								items.add(new Item(title, url, desc, feedTitle));
							}
							title = null;
							url = null;
							desc = null;
							insideItem = false;
						}
						eventType = xpp.next();
					}
				} catch (MalformedURLException e) {
					System.out.println("Bad: " + feeds[0].getTitle());
					System.out.println("Bad: " + feeds[0].getUrl());
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					System.out.println("Bad: " + feeds[0].getTitle());
					System.out.println("Bad: " + feeds[0].getUrl());
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Bad: " + feeds[0].getTitle());
					System.out.println("Bad: " + feeds[0].getUrl());
					e.printStackTrace();
				}
			}
			// adds it to the database
			fds.createRSS(feeds[0].getTitle(),feeds[0].getUrl());
			for (Item i : items){
				ids.createItem(i.getTitle(), i.getUrl(), i.getDesc(), i.getFeedTitle());
			}
			return items;
		}

		@Override
		protected void onPostExecute(List<Item> itemList) {
			super.onPostExecute(itemList);
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
