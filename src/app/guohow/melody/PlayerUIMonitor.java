package app.guohow.melody;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import app.guohow.melody.player.MusicPlayer;

import com.guohow.melody_sildemenu.R;

public class PlayerUIMonitor {

	public static String playingSongTitle = "melody rhythm";
	public static String playingSongArtist = "art-melody";
	static Button btn_play, btn_previous, btn_next;

	public PlayerUIMonitor(Button btn_play, Button btn_previous, Button btn_next) {
		PlayerUIMonitor.btn_next = btn_next;
		PlayerUIMonitor.btn_play = btn_play;
		PlayerUIMonitor.btn_previous = btn_previous;
		btnControler();
		
	}

	private static void btnControler() {

		btnCheck();
		updatePlayingSongInfo();
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

					}
				}

			});

		}

	}

	public static void btnCheck() {

		if (MusicPlayer.FLAG == 0) {
			btn_play.setBackgroundResource(R.drawable.btn_play);

		} else if (MusicPlayer.FLAG == 1) {
			btn_play.setBackgroundResource(R.drawable.btn_pause);

		}
	}

	public static void updatePlayingSongInfo() {
		if(MusicPlayer.hasEverPlayed){
		playingSongTitle = (String) MusicPlayer.data.get(MusicPlayer.current)
				.get("title");
		playingSongArtist = (String) MusicPlayer.data.get(MusicPlayer.current)
				.get("artist");
		}
	}
}
