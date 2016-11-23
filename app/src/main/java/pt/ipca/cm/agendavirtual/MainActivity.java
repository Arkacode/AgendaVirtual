package pt.ipca.cm.agendavirtual;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AgendaFragment agendaFragment=new AgendaFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.
                beginTransaction().
                replace(R.id.content_frame,agendaFragment).
                setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                commit();
    }
}
