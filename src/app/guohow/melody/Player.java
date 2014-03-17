package app.guohow.melody;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import app.guohow.melody.player.MusicPlayer;

import com.guohow.melody_sildemenu.R;

public class Player extends Fragment {
	protected View mRootView;

	protected static ArrayList<Map<String, Object>> mlistItems;
	protected Context mContext;

	public static String playingSongInfo = "melody rhythm";
	public static String playingArtInfo = "art-melody";
	public static boolean hasNotQuit = true;

	Handler handler = new Handler();

	Button btn_play, btn_next, btn_previous, btn_mod, btn_delete, btn_favour,
			btn_bell;
	TextView titleView, artistView;
	SeekBar seekBar;

	public Player() {
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
		mRootView = inflater
				.inflate(R.layout.player, container, false);
		hasNotQuit = true;
		seekBarInit();
		btnControler();
		return mRootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// handler更新进度条以及播放按钮
		handler.post(new Runnable() {
			public void run() {
				// 更新进度条状态
				if (true)
					// 先更新btn
					btnCheck();
				// 更新textView
				playingSongCheck();
				seekBar.setProgress(MusicPlayer.player.getCurrentPosition());

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
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// handler更新进度条以及播放按钮
		handler.post(new Runnable() {
			public void run() {
				// 更新进度条状态
				if (true)
					// 先更新btn
					btnCheck();
				// 更新textView
				playingSongCheck();
				seekBar.setProgress(MusicPlayer.player.getCurrentPosition());

				// 1秒之后再次发送
				handler.postDelayed(this, 1000);
			}
		});

		super.onViewStateRestored(savedInstanceState);
	}

	private void btnControler() {

		btn_play = (Button) mRootView.findViewById(R.id.play);
		btn_next = (Button) mRootView.findViewById(R.id.next);
		btn_previous = (Button) mRootView.findViewById(R.id.previous);
		// btn_mod = (Button) mRootView.findViewById(R.id.mod);

		btnCheck();

		// 按钮监听
		// 正在播放则暂停
		if (btn_play != null) {
			btn_play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MusicPlayer.FLAG == 1) {
						MusicPlayer.pause();
						MusicPlayer.FLAG = 0;
						btn_play.setBackgroundResource(R.drawable.btn_play);
					} else if (MusicPlayer.FLAG == 0
							&& MusicPlayer.hasEverPlayed) {
						MusicPlayer.resume();
						MusicPlayer.FLAG = 1;
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else if (MusicPlayer.FLAG == 0
							&& !MusicPlayer.hasEverPlayed) {

						Toast.makeText(mRootView.getContext(), "你想听哪首呢",
								Toast.LENGTH_SHORT).show();
						// MusicPlayer.playRandom();
						// btn_mod.setBackgroundResource(R.drawable.play_shuffle);
					}
				}
			});
		}
		// next按钮监听
		if (btn_next != null) {

			btn_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MusicPlayer.hasEverPlayed) {
						MusicPlayer.next();
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						Toast.makeText(mRootView.getContext(), "你想听哪首呢",
								Toast.LENGTH_SHORT).show();
					}
				}

			});

		}

		if (btn_previous != null) {

			btn_previous.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MusicPlayer.hasEverPlayed) {
						MusicPlayer.previous();
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						Toast.makeText(mRootView.getContext(), "你想听哪首呢？",
								Toast.LENGTH_SHORT).show();
					}
					// btn_mod.setBackgroundResource(R.drawable.btn_shuffle);
				}

			});

		}

	}

	public void btnCheck() {

		if (MusicPlayer.FLAG == 0) {
			btn_play.setBackgroundResource(R.drawable.btn_play);

		} else if (MusicPlayer.FLAG == 1) {
			btn_play.setBackgroundResource(R.drawable.btn_pause);

		}
	}

	// 检测正在播放，并设置textView
	public void playingSongCheck() {

		titleView = (TextView) mRootView.findViewById(R.id.songTitle);
		artistView = (TextView) mRootView.findViewById(R.id.artistView);
		// titleView.setText(MusicPlayer.data.get(MusicPlayer.current)
		// .get("title"));
		// artistView
		// .setText(MusicPlayer.data.get(MusicPlayer.current).get("art"));
		if (MusicPlayer.data != null && hasNotQuit) {
			playingArtInfo = (String) MusicPlayer.data.get(MusicPlayer.current).get(
					"artist");
			playingSongInfo = (String) MusicPlayer.data.get(MusicPlayer.current).get(
					"title");
		}
		artistView.setText(playingArtInfo);
		titleView.setText(playingSongInfo);
		// 显示进度条
		if (MusicPlayer.hasEverPlayed) {
			seekBar.setVisibility(View.VISIBLE);
		}
	}

	private void seekBarInit() {
		seekBar = (SeekBar) mRootView.findViewById(R.id.mSeekBar);
		MusicPlayer.seekBar = seekBar;

	}

	public void phoneCheck() {

	}

}
