package com.example.morningreport.cardcreators;

import com.example.morningreport.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RSSItemCardCreator implements CardCreator {
	
	private String _providerName;
	private int _providerLogo;
	private String _postTitle;
	private String _url;

	public RSSItemCardCreator (String providerName, int providerLogo, String postTitle, String url) {
		this._providerName = providerName;
		this._providerLogo = providerLogo;
		this._postTitle = postTitle;
		this._url = url;
	}
	
	@Override
	public View createView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    LinearLayout card = (LinearLayout) inflater.inflate(R.layout.rss_card, null);
	    
	    ImageView logo = (ImageView) card.findViewById(R.id.logo);
	    logo.setImageResource(this._providerLogo);
		TextView providerName = (TextView)card.findViewById(R.id.providerName);
		providerName.setText(this._providerName);
	    
		TextView postTitle = (TextView)card.findViewById(R.id.title);
		postTitle.setText(this._postTitle);
		
		return card;
	}
	
	public String getUrl() {
		return this._url; 
	}
	

}
