package app.guohow.melody;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import app.guohow.melody.settings.Settings;

import com.guohow.melody_sildemenu.R;

public class MelodyMenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String[] menus = getResources().getStringArray(R.array.menu_names);

		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, menus);

		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new FavourList();
			break;
		
		case 1:
			newContent = new AllSongsList();
			break;
		case 2:
			Intent intent=new Intent();
			intent.setClass(getActivity(),Settings.class);
			startActivity(intent);
			break;
	
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof Melody) {
			Melody fca = (Melody) getActivity();
			fca.switchContent(fragment);
		}
	}

}
