package app.guohow.melody.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

public class ToneBean extends Activity {

	public static final int RINGTONE = 0; // 铃声

	public static final int NOTIFICATION = 1; // 通知音

	public static final int ALARM = 2; // 闹钟

	public static final int ALL = 3; // 所有声音

	public void setVoice(String _path, int id, ContentResolver cr)

	{

		ContentValues cv = new ContentValues();

		Uri newUri = null;

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(_path);

		// 查询音乐文件在媒体库是否存在

		Cursor cursor = cr.query(uri, null,
				MediaStore.MediaColumns.DATA + "=?", new String[] { _path },
				null);

		if (cursor.moveToFirst() && cursor.getCount() > 0)

		{

			String _id = cursor.getString(0);

			switch (id) {

			case ToneBean.RINGTONE:

				cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);

				cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);

				cv.put(MediaStore.Audio.Media.IS_ALARM, false);

				cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

				break;

			case ToneBean.NOTIFICATION:

				cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);

				cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);

				cv.put(MediaStore.Audio.Media.IS_ALARM, false);

				cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

				break;

			case ToneBean.ALARM:

				cv.put(MediaStore.Audio.Media.IS_RINGTONE, false);

				cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);

				cv.put(MediaStore.Audio.Media.IS_ALARM, true);

				cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

				break;

			case ToneBean.ALL:

				cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);

				cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);

				cv.put(MediaStore.Audio.Media.IS_ALARM, true);

				cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

				break;

			default:

				break;

			}

			// 把需要设为铃声的歌曲更新铃声库

			cr.update(uri, cv, MediaStore.MediaColumns.DATA + "=?",
					new String[] { _path });

			newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));

			// 一下为关键代码：

			switch (id) {

			case ToneBean.RINGTONE:

				RingtoneManager.setActualDefaultRingtoneUri(this,
						RingtoneManager.TYPE_RINGTONE, newUri);

				break;

			case ToneBean.NOTIFICATION:

				RingtoneManager.setActualDefaultRingtoneUri(this,
						RingtoneManager.TYPE_NOTIFICATION, newUri);

				break;

			case ToneBean.ALARM:

				RingtoneManager.setActualDefaultRingtoneUri(this,
						RingtoneManager.TYPE_ALARM, newUri);

				break;

			case ToneBean.ALL:

				RingtoneManager.setActualDefaultRingtoneUri(this,
						RingtoneManager.TYPE_ALL, newUri);

				break;

			default:

				break;

			}
		}
	}

}
