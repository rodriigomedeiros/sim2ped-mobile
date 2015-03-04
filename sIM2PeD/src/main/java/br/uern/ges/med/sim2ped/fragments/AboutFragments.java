package br.uern.ges.med.sim2ped.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import br.uern.ges.med.sim2ped.R;


@SuppressLint("NewApi")
public class AboutFragments extends Fragment {

    public AboutFragments(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView;

		rootView = inflater.inflate(R.layout.layout_about, container, false);

        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setBackgroundColor(0x00000000);

        String customHtml = rootView.getContext().getString(R.string.html_about_app);
        webView.loadData(customHtml, "text/html", "UTF-8");
		
		return rootView;
	}
}
