package pt.ipca.cm.agendavirtual;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by miguel on 06/10/16.
 */

public class HttpFetchData extends AsyncTask<String,String,List<AgendaItem>> {

    List listners=new ArrayList();

    public synchronized void addHttpResponseEvent(HttpListner listner){
        listners.add(listner);
    }

    public synchronized void removeHttpResponseEvent(HttpListner listner){
        listners.remove(listner);
    }

    private synchronized void fireSuccessResponseEvent(List<AgendaItem> noticiaItemList){
        for(Object listner:listners){
            ((HttpListner)listner).onHttpResponseEvent(noticiaItemList);
        }
    }

    public HttpFetchData(){

    }

    @Override
    protected List<AgendaItem> doInBackground(String... params) {

        List<AgendaItem> noticiaItemList=new ArrayList<>();

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(params[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            InputStream is=urlConnection.getInputStream();
            Document doc=builder.parse(is);

            NodeList nodes=doc.getElementsByTagName("item");
            for(int i=0; i < nodes.getLength(); i++){
                AgendaItem noticiaItem=new AgendaItem();
                Element element=(Element) nodes.item(i);
                NodeList title=element.getElementsByTagName("title");
                NodeList description=element.getElementsByTagName("description");
                NodeList link=element.getElementsByTagName("link");
                NodeList city= element.getElementsByTagName("content:encoded");
                NodeList location = element.getElementsByTagName("content:encoded");
                NodeList image = element.getElementsByTagName("content:encoded");
                NodeList pubDate = element.getElementsByTagName("pubDate");
                //ir buscar o texto do xml do RSS
                String xmlDescription = description.item(0).getTextContent();
                String xmlImage = image.item(0).getTextContent();
                String xmlLocation = location.item(0).getTextContent();
                String xmlCity = city.item(0).getTextContent();
                //regex do conteudo
                Matcher matcherImagem = Pattern.compile("img src=\"([^\"]+)").matcher(xmlImage);
                Matcher matcherCity = Pattern.compile("<div class=\"city\">(.+?)</div>").matcher(xmlCity);
                Matcher matcherLocation = Pattern.compile("<div class=\"location\">(.+?)</div>").matcher(xmlLocation);
                String imagem = "";
                String localidade = "";
                String cidade = "";
                //conteudo
                while (matcherImagem.find()) {
                    imagem = matcherImagem.group(1);
                }
                while(matcherCity.find()){
                    cidade =  matcherCity.group(1);
                }
                while(matcherLocation.find()){
                    localidade = matcherLocation.group(1);
                }
                noticiaItem.setTitle(title.item(0).getTextContent());
                noticiaItem.setDescription(description.item(0).getTextContent());
                noticiaItem.setUrl(link.item(0).getTextContent());
                noticiaItem.setImageLink(imagem);
                noticiaItem.setCity(cidade);
                noticiaItem.setLocation(localidade);
                DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
                Date date = formatter.parse(pubDate.item(0).getTextContent());
                noticiaItem.setDatePub(date);
                noticiaItemList.add(noticiaItem);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return noticiaItemList;
    }

    @Override
    protected void onPostExecute(List<AgendaItem> noticiaItemList) {
        super.onPostExecute(noticiaItemList);

        fireSuccessResponseEvent(noticiaItemList);

    }
}
