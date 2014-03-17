package app.guohow.melody.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicPlayer extends Activity {

	public static MediaPlayer player = new MediaPlayer();
	public static List<HashMap<String, Object>> data;
	public static int current;

	public static SeekBar seekBar;

	public static int FLAG = 0;// 0表示未播放
	public static int RANDOM = 0;// 0表示未播放
	public static boolean hasEverPlayed = false;

	public static boolean isStartTrackingTouch;

	public MusicPlayer(int current, List<HashMap<String, Object>> data) {
		MusicPlayer.current = current;
		MusicPlayer.data = data;

	}

	public void play() {

		player.reset();
		try {
			player.setDataSource((String) data.get(current).get("url"));
			// 缓冲
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.start();
		// 已经播放过任何歌曲
		hasEverPlayed = true;
		// 设置进度条长度
		if (seekBar != null) {
			seekBar.setMax(player.getDuration());
			seekBar.setOnSeekBarChangeListener(new MySeekBarListener());
		}

		// 播放器监听器
		player.setOnCompletionListener(new MyPlayerListener());

		FLAG = 1;
	}

	public static void stop() {
		player.stop();

	}

	public static void pause() {
		player.pause();
	}

	public static void resume() {

		player.start();
	}

	public static void previous() {
		current = current - 1 < 0 ? data.size() - 1 : current - 1;
		player.stop();
		new MusicPlayer(current, data).play();
	}

	public static void next() {
		// 如果到达列表最低端，则重新开始（重复播放）
		if (current == data.size() - 1) {
			current = 0 - 1;
		}

		current = current + 1 % data.size();
		if (player != null) {
			player.stop();
			new MusicPlayer(current, data).play();
		} else if (player == null) {
			playRandom();
			RANDOM = 1;// 正在随机
		}

	}

	public static void playRandom() {
		current = new Random().nextInt();
		// 如果创建过对象
		if (player != null) {
			player.stop();
			new MusicPlayer(current, data).play();
		} else if (player == null) {
			// 如果未创建过对象
			new MusicPlayer(current, data).play();
		}

	}

	public class MySeekBarListener implements OnSeekBarChangeListener {
		// 移动触发
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		// 起始触发
		public void onStartTrackingTouch(SeekBar seekBar) {
			isStartTrackingTouch = true;
		}

		// 结束触发
		public void onStopTrackingTouch(SeekBar seekBar) {
			MusicPlayer.player.seekTo(seekBar.getProgress());
			isStartTrackingTouch = false;
		}
	}

	private final class MyPlayerListener implements OnCompletionListener {
		// 歌曲播放完后自动播放下一首歌曲

		public void onCompletion(MediaPlayer mp) {
			next();
		}
	}

}