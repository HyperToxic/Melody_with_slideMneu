package app.guohow.melody;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import app.guohow.melody.database.SQLHelper;
import app.guohow.melody.player.MusicPlayer;
import app.guohow.melody.utils.Mp3Bean;
import app.guohow.melody.utils.MusicUtils;

import com.guohow.melody_sildemenu.R;

public class AllSongsList extends Fragment {

	public View mRootView;
	protected Context mContext;
	List<HashMap<String, Object>> data;
	Button btn_play, btn_next, btn_previous;
	SeekBar seekBar;
	TextView bottomInfo;
	ListView listView = null;

	private static SQLHelper dbHelper;
	private static String DB_NAME = "mTable.db";
	private static int DB_VERSION = 1;
	public static int _index;
	public static SQLiteDatabase db;

	Handler handler = new Handler();

	public AllSongsList() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.songslist, container, false);
		//设置actionBar图标
		getActivity().getActionBar().setLogo(R.drawable.ic_local);
		getActivity().getActionBar().setTitle("本地音乐");
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initMusicListAdapter();
		listItemInit();
		onItemLongPressedControler();
		btn_Control();
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// handler更新底兰
		handler.post(new Runnable() {
			public void run() {
				PlayerUIMonitor.btnCheck();
				if (MusicPlayer.data != null && bottomInfo != null) {
					bottomInfo.setText("正在播放:"
							+ PlayerUIMonitor.playingSongTitle + "  " + "艺术家:"
							+ PlayerUIMonitor.playingSongArtist);

				}
				if (seekBar != null) {
					seekBar.setProgress(MusicPlayer.player.getCurrentPosition());
				}
				// 1秒之后再次发送
				handler.postDelayed(this, 1000);
			}
		});

		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
	 */

	public void initMusicListAdapter() {

		listView = (ListView) mRootView.findViewById(R.id.allSongsList);
		// mSlideBar = (SlideBarView) mRootView.findViewById(R.id.slideBar);

		bottomInfo = (TextView) mRootView.findViewById(R.id.info_all);
		// 獲取CONTEXT
		Context con = mRootView.getContext();
		// 获取CURSOR
		Cursor cursor = con.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Mp3Bean> mp3InfoList = MusicUtils.getMp3Infos(cursor, con);
		MusicUtils.listUpdate(mp3InfoList);
		// 更新data
		data = MusicUtils.mp3list;
		bottomInfo.setText("本地队列：" + data.size() + "首");
		// 自定义ADP
		// 2行显示，曲目和作者
		SimpleAdapter adapter = new SimpleAdapter(con, data,
				R.layout.songsitem, new String[] { "title", "artist",
						"duration" }, new int[] { R.id.mTitle, R.id.mArt,
						R.id.mDuration });
		listView.setAdapter(adapter);

	}

	private void listItemInit() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				// 每次只会运行一个实例？？？
				MusicPlayer.hasEverPlayed = true;
				if (MusicPlayer.FLAG == 0) {
					new MusicPlayer(position, data).play();
					MusicPlayer.FLAG = 1;
					// 设置listView的当前位置
					listView.setSelection(position);
				} else if (MusicPlayer.FLAG == 1) {

					if (position == MusicPlayer.current) {
						MusicPlayer.pause();
						MusicPlayer.FLAG = 0;
					} else {
						new MusicPlayer(position, data).play();
						// 设置FLAG为1
						MusicPlayer.FLAG = 1;
					}

				}
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE: //
					if (bottomInfo != null) {
						bottomInfo.setVisibility(View.VISIBLE);
					}
					if (seekBar != null && !MusicPlayer.hasEverPlayed) {
						seekBar.setVisibility(View.GONE);
					}else{
						seekBar.setVisibility(View.GONE);
					}

					System.out.println("停止...");
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					if (bottomInfo != null) {
						bottomInfo.setVisibility(View.GONE);
						seekBar.setVisibility(View.GONE);
					}

					System.out.println("正在滑动...");
					break;
				case OnScrollListener.SCROLL_STATE_FLING:

					System.out.println("开始滚动...");

					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void btn_Control() {
		btn_play = (Button) mRootView.findViewById(R.id.play);
		btn_next = (Button) mRootView.findViewById(R.id.next);
		btn_previous = (Button) mRootView.findViewById(R.id.previous);
		seekBar = (SeekBar) mRootView.findViewById(R.id.seekBar1);
		new PlayerUIMonitor(btn_play, btn_previous, btn_next, seekBar);
	}

	private void onItemLongPressedControler() {
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View arg1,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				_index = ((AdapterContextMenuInfo) menuInfo).position;// 获取menu点击项的position
				menu.setHeaderTitle(data.get(_index).get("title").toString());
				menu.add(0, 0, 0, "标记为我喜欢");
				menu.add(0, 3, 0, "分享");

			}
		});

	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			// 初始化&创建数据库
			dbHelper = new SQLHelper(mRootView.getContext(), DB_NAME, null,
					DB_VERSION);
			// 创建表
			db = dbHelper.getWritableDatabase(); // 自动调用SQLiteHelper.OnCreate()
			// 添加到我喜欢
			ContentValues values = new ContentValues();
			values.put("title", (String) data.get(_index).get("title"));
			values.put("artist", (String) data.get(_index).get("artist"));
			values.put("url", (String) data.get(_index).get("url"));

			db.insert(SQLHelper.TABLE_NAME, null, values);
			FavourList.UPDATE = 1;
			break;

		case 3:
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			// intent.setType();//其他类型是什么？

			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT,
					"这首歌挺好听，推荐给你！歌名是：" + data.get(_index).get("title")
							+ ",一定要听哦！ shared by Melody!");
			startActivity(Intent.createChooser(intent, "分享到"));
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

}
