package lucas.br.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import lucas.br.whatsapp.fragment.ContatosFragment;
import lucas.br.whatsapp.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String [] tituloTabs = {"CONVERSAS","CONTATOS"};


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment =  null;

        switch (position){

            case 0:
                fragment = new ConversasFragment();
                break;

            case 1:
                fragment = new ContatosFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloTabs[position];
    }
}
