package app.guohow.melody;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;
import app.guohow.melody.player.MusicPlayer;

import com.guohow.melody_sildemenu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class Melody extends SlidingFragmentActivity {

	private Fragment mContent;
	BroadcastReceiver br1;
	BroadcastReceiver br2;

	NotificationManager mNotificationManager;

	boolean fullScreen;
	public static boolean NEED_NOTI;
	public static String CURRENT_TITLE, ARTIST;

	// Handler
	Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("");

		initSlidingMenu(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// 显示通知
		initNotification();

		// 意图过滤器
		IntentFilter filter = new IntentFilter();
		IntentFilter filter2 = new IntentFilter();
		br1 = new PhoneListener();
		// 耳机监听
		br2 = new HeadsetPlugReceiver();

		filter2.addAction("android.intent.action.HEADSET_PLUG");
		// 播出电话暂停音乐播放
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");

		registerReceiver(br1, filter);
		registerReceiver(br2, filter2);

		// 创建一个电话服务
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 监听电话状态，接电话时停止播放
		manager.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);

		// 状态栏通知
		handler.post(new Runnable() {
			public void run() {
				checkNoti();
				handler.postDelayed(this, 1000);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 退出时暂停，等待恢复，继续播放
			MusicPlayer.stop();
			MusicPlayer.FLAG = 0;
			finish();
			return true;
		}
		if(keyCode==KeyEvent.KEYCODE_MENU&& event.getRepeatCount()==0){
			
		//	showMenu();
			toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(br1);
		unregisterReceiver(br2);
		super.onDestroy();
	}

	/**
	 * 
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			// mContent = new MelodyFragment(R.color.white);
			mContent = new AllSongsList();

		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MelodyMenuFragment()).commit();

		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);

	}

	/**
	 * 
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 *
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ״̬
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	// 来自android Api文档
	public void initNotification() {
		Intent resultIntent;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("MELODY").setContentText("now playing");
		// Creates an explicit intent for an Activity in your appIntent
		resultIntent = new Intent(this, Melody.class);
		// The stack builder object will contain an artificial back stack for
		// the// started Activity.// This ensures that navigating backward from
		// the Activity leads out of// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Melody.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}

	public void checkNoti() {
		if (NEED_NOTI) {

			updateNoti(CURRENT_TITLE, ARTIST);
		}

	}

	public void updateNoti(String currentTitle, String artist) {

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
				this).setContentTitle(currentTitle).setContentText(artist)
				.setSmallIcon(R.drawable.ic_launcher);
		// Start of a loop that processes data and then notifies the user...
		// Because the ID remains unchanged, the existing notification is
		// updated.
		mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}

	public boolean getIfFullScreen() {

		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(this);
		fullScreen = spf.getBoolean("full_screen_perf", true);
		return fullScreen;
	}

	private final class PhoneListener extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			MusicPlayer.pause();
			MusicPlayer.FLAG = 0;
		}
	}

	private final class MyPhoneStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String incomingNumber) {
			MusicPlayer.pause();
			MusicPlayer.FLAG = 0;
		}
	}

	private class HeadsetPlugReceiver extends BroadcastReceiver {

		// private static final String TAG = "HeadsetPlugReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("state")) {
				if (intent.getIntExtra("state", 0) == 0) {
					Toast.makeText(context, "耳机未连接", Toast.LENGTH_SHORT).show();
					MusicPlayer.pause();
					MusicPlayer.FLAG = 0;
				} else if (intent.getIntExtra("state", 0) == 1) {
					Toast.makeText(context, "耳机已连接", Toast.LENGTH_SHORT).show();
				}
			}

		}

	}
}