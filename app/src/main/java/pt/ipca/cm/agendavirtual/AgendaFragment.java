package pt.ipca.cm.agendavirtual;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {
    String urlPub="http://192.168.1.68/feed"; // 192.168.1.69
    ListView listViewFeed;
    List<AgendaItem> noticias=new ArrayList<>();
    ListViewAdapter adapter;


    public AgendaFragment() {
        // Required empty public constructor


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
                convertView = mInflater.inflate(R.layout.row_feed, parent,false);

            TextView textViewTitle=(TextView)convertView.findViewById(R.id.textViewTitleInfo);
            ImageView imageViewFeed = (ImageView) convertView.findViewById(R.id.imgPub);
            TextView textViewData=(TextView)convertView.findViewById(R.id.textViewData);
            TextView textViewHora=(TextView)convertView.findViewById(R.id.textViewHora);
            TextView textViewLocation=(TextView)convertView.findViewById(R.id.textViewLocation);
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleHour =  new SimpleDateFormat("HH:mm");
            String data = simpleDate.format(noticias.get(position).datePub);
            String hora = simpleHour.format(noticias.get(position).datePub);
            textViewData.setText(data);
            textViewHora.setText(hora);
            textViewLocation.setText(noticias.get(position).location);
            textViewTitle.setText(noticias.get(position).getTitle());
            TextView textViewCity = (TextView) convertView.findViewById(R.id.textViewDesc);
            textViewCity.setText(noticias.get(position).getCity());
            String url = noticias.get(position).getImageLink();
            imageLoader.DisplayImage(url, imageViewFeed);
            convertView.setTag(new Integer(position));
            convertView.setClickable(true);
            convertView.setOnClickListener(this);

            return convertView;
        }

        @Override
        public void onClick(View v) {
            Integer position=(Integer) v.getTag();
            Log.d("NewsFeed","Desc:"+noticias.get(position).getDescription());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getCity());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getDatePub());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getLocation());
            Log.d("NewsFeed","Clicked:"+noticias.get(position).getImageLink());
            //inserir tudo num bundle a informação do publicação
            Bundle bundle = new Bundle();
            bundle.putString(InfoArticleFragment.EXTRA_TITLE,noticias.get(position).getTitle());
            bundle.putString(InfoArticleFragment.EXTRA_DESC,noticias.get(position).getDescription());
            bundle.putString(InfoArticleFragment.EXTRA_PUB_DATE,noticias.get(position).getDatePub().toString());
            bundle.putString(InfoArticleFragment.EXTRA_LINK_IMAGE,noticias.get(position).getImageLink());
            bundle.putString(InfoArticleFragment.EXTRA_CITY,noticias.get(position).getCity());
            bundle.putString(InfoArticleFragment.EXTRA_LOCATION,noticias.get(position).getLocation());
            //mudar de fragment
            InfoArticleFragment infoArticleFragment = new InfoArticleFragment();
            FragmentManager fragmentManager=getFragmentManager();
            infoArticleFragment.setArguments(bundle);
            fragmentManager.
                    beginTransaction().
                    replace(R.id.content_frame,infoArticleFragment).
                    setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                    addToBackStack(null).
                    commit();

        }
    }

}
