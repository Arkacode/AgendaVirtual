package pt.ipca.cm.agendavirtual;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {
    String urlPub="http://testefilmes123.netai.net/"; // 192.168.1.69
    ListView listViewFeed;
    List<AgendaItem> noticias=new ArrayList<>();
    ListViewAdapter adapter;


    public AgendaFragment() {
        // Required empty public constructor
//commit test

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);


        HttpFetchData httpFetchData=new HttpFetchData();
        httpFetchData.execute(urlPub,null,null);
        httpFetchData.addHttpResponseEvent(new HttpListner() {
            @Override
            public void onHttpResponseEvent(List<AgendaItem> noticiaItems) {
                noticias=noticiaItems;
                adapter.notifyDataSetChanged();
            }
        });

        listViewFeed=(ListView)rootView.findViewById(R.id.listViewFeed);
        adapter=new ListViewAdapter();
        listViewFeed.setAdapter(adapter);

        getActivity().setTitle("Agenda Virtual");
        return rootView;
    }


    class ListViewAdapter extends BaseAdapter implements View.OnClickListener{

        LayoutInflater mInflater;
        ImageLoader imageLoader;

        public ListViewAdapter(){
            mInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(getActivity().getApplicationContext());
        }

        @Override
        public int getCount() {
            return noticias.size();
        }

        @Override
        public Object getItem(int position) {
            return noticias.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = mInflater.inflate(R.layout.row_feed, null);

            TextView textViewTitle=(TextView)convertView.findViewById(R.id.textViewTitle);
            ImageView imageViewFeed = (ImageView) convertView.findViewById(R.id.imgPub);
            textViewTitle.setText(noticias.get(position).getTitle());
            TextView textViewCity = (TextView) convertView.findViewById(R.id.textViewDesc);
            textViewCity.setText(noticias.get(position).getCity());
            String url = noticias.get(position).getImageLink();
            imageLoader.DisplayImage(url, imageViewFeed);
            String teste;
            convertView.setTag(new Integer(position));
            convertView.setClickable(true);
            convertView.setOnClickListener(this);

            return convertView;
        }

        @Override
        public void onClick(View v) {
            Integer position=(Integer) v.getTag();
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getCity());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getDatePub());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getLocation());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getImageLink());
            /*Bundle bundle = new Bundle();
            bundle.putString(WebViewFragment.EXTRA_URL,noticias.get(position).getUrl());
            bundle.putString(WebViewFragment.EXTRA_TITLE,noticias.get(position).getTitle());

            WebViewFragment webViewFragment=new WebViewFragment();
            FragmentManager fragmentManager=getFragmentManager();
            webViewFragment.setArguments(bundle);
            fragmentManager.
                    beginTransaction().
                    replace(R.id.content_frame,webViewFragment).
                    setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                    addToBackStack(null).
                    commit();*/

        }
    }

}
