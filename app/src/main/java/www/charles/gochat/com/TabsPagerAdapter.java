package www.charles.gochat.com;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



class TabsPagerAdapter extends FragmentPagerAdapter {
    /**
     * generating a contructor for our pager adapter
     */

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    /*
    *This method will get the position of
    * the fragment that the user has selected
     */
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                StatusFragment statusFragment = new StatusFragment();
                return statusFragment;
            case 3:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return 4;
    }
    /**
     * This method will help set the title to our pages
     * In the view pager
     */
    public CharSequence getPageTitle(int position){
        switch (position)
        {
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case 2:
                return "Status";
            case 3:
                return "Friends";
            default:
                return null;
        }
    }
}
