package pt.ipca.cm.agendavirtual;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class InfoArticleFragment extends Fragment {

    public static final java.lang.String EXTRA_TITLE = "title";
    public static final java.lang.String EXTRA_DESC = "desc";
    public static final java.lang.String EXTRA_LINK_IMAGE = "link_image";
    public static final java.lang.String EXTRA_PUB_DATE = "pub_date";
    public static final java.lang.String EXTRA_CITY = "city";
    public static final java.lang.String EXTRA_LOCATION = "location";

    String title="";
    String desc="";
    String link_image="";
    String pubDate="";
    String city="";
    String location="";

    ImageLoader imageLoader;


    public InfoArticleFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new ImageLoader(getActivity().getApplicationContext());
        //bundle com informacao
        Bundle bundle=getArguments();
        title=bundle.getString(EXTRA_TITLE);
        desc=bundle.getString(EXTRA_DESC);
        link_image=bundle.getString(EXTRA_LINK_IMAGE);
        pubDate=bundle.getString(EXTRA_PUB_DATE);
        city=bundle.getString(EXTRA_CITY);
        location=bundle.getString(EXTRA_LOCATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info_article, container, false);
        TextView textViewTitleInfo = (TextView) rootView.findViewById(R.id.textViewTitleInfo);
        textViewTitleInfo.setText(title);
        ImageView imageViewAgenda = (ImageView) rootView.findViewById(R.id.imageViewAgendaInfo);
        String url = link_image;
        TextView textViewDescription = (TextView) rootView.findViewById(R.id.textViewDescInfo);
        textViewDescription.setText(desc);
        TextView textViewLocation = (TextView) rootView.findViewById(R.id.textViewLocationInfo);
        textViewLocation.setText(location);
        TextView textViewCity = (TextView) rootView.findViewById(R.id.textViewCityInfo);
        textViewCity.setText(city);
        TextView textViewDate = (TextView) rootView.findViewById(R.id.textViewDateInfo);
        textViewDate.setText(pubDate);
        TextView textViewHour = (TextView) rootView.findViewById(R.id.textViewHourInfo);
        textViewHour.setText(pubDate);
        imageLoader.DisplayImage(url, imageViewAgenda);
        ((MainActivity) getActivity()).setTitle("Informação");
        return rootView;
    }



}
