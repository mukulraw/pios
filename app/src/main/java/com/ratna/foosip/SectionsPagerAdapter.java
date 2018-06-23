package com.ratna.foosip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AkshayeJH on 11/06/17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                Wall requestsFragment = new Wall();
                return requestsFragment;

            case 1:
                UsersFragment chatsFragment = new UsersFragment();
                return  chatsFragment;

            case 2:
                ChatFragment friendsFragment = new ChatFragment();
                return friendsFragment;

            case 3:
                RequestsFragment groupFragment = new RequestsFragment();
                return groupFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }


    /*public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "REQUESTS";

            case 1:
                return "CHATS";

            case 2:
                return "FRIENDS";

            case 3:
                return "Restaurant";

            default:
                return null;
        }

    }*/

}
