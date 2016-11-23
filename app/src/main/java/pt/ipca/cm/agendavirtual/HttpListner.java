package pt.ipca.cm.agendavirtual;

import java.util.List;

/**
 * Created by miguel on 06/10/16.
 */

public interface HttpListner {
    public void onHttpResponseEvent(List<AgendaItem> noticiaItems);
}
