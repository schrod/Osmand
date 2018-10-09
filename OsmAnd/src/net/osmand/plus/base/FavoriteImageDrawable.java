package net.osmand.plus.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

import net.osmand.IndexConstants;
import net.osmand.plus.OsmandApplication;

import java.io.File;
import java.util.TreeMap;

import net.osmand.plus.R;

public class FavoriteImageDrawable extends Drawable {

	private boolean withShadow;
	private boolean synced;
	private boolean history;
	private Bitmap favIcon;
	private Bitmap favBackground;
	private Bitmap syncedStroke;
	private Bitmap syncedColor;
	private Bitmap syncedShadow;
	private Bitmap syncedIcon;
	private Drawable listDrawable;
	private Paint paintIcon = new Paint();
	private Paint paintBackground = new Paint();
	private Paint paintOuter = new Paint();
	private Paint paintInnerCircle = new Paint();
	private ColorFilter colorFilter;
	private ColorFilter grayFilter;
	private Bitmap icon;

	private enum FavoriteImageType {
		DRAWN,
		ICON
	}
	private FavoriteImageType type;

	public FavoriteImageDrawable(Context ctx, int color, String customIconFileName, boolean withShadow, boolean synced) {
		this.withShadow = withShadow;
		this.synced = synced;
		Resources res = ctx.getResources();
		int col = color == 0 || color == Color.BLACK ? res.getColor(R.color.color_favorite) : color;
		favIcon = BitmapFactory.decodeResource(res, R.drawable.map_favorite);
		favBackground = BitmapFactory.decodeResource(res, R.drawable.map_white_favorite_shield);
		syncedStroke = BitmapFactory.decodeResource(res, R.drawable.map_shield_marker_point_stroke);
		syncedColor = BitmapFactory.decodeResource(res, R.drawable.map_shield_marker_point_color);
		syncedShadow = BitmapFactory.decodeResource(res, R.drawable.map_shield_marker_point_shadow);
		syncedIcon = BitmapFactory.decodeResource(res, R.drawable.map_marker_point_14dp);
		listDrawable = ResourcesCompat.getDrawable(res, R.drawable.ic_action_fav_dark, null).mutate();
		initSimplePaint(paintOuter, color == 0 || color == Color.BLACK ? 0x88555555 : color);
		initSimplePaint(paintInnerCircle, col);
		colorFilter = new PorterDuffColorFilter(col, PorterDuff.Mode.MULTIPLY);
		grayFilter = new PorterDuffColorFilter(res.getColor(R.color.color_favorite_gray), PorterDuff.Mode.MULTIPLY);

		if (!customIconFileName.isEmpty()) {
			OsmandApplication app = (OsmandApplication) ctx.getApplicationContext();
			final File iconFile = new File(app.getAppPath(IndexConstants.ICON_DIR), customIconFileName);
			if (iconFile.exists()) {
				icon = BitmapFactory.decodeFile(iconFile.getAbsolutePath());
				if (icon != null) {
					//Scale the icon to match the size of the drawn icons
					icon = Bitmap.createScaledBitmap(icon, favBackground.getWidth(), favBackground.getHeight(), true);
					this.type = FavoriteImageType.ICON;
				}
			}
		}
	}


	private void initSimplePaint(Paint paint, int color) {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(color);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		if (!withShadow && !synced) {
			Rect bs = new Rect(bounds);
			//bs.inset((int) (4 * density), (int) (4 * density));
			bs.inset(bs.width() / 4, bs.height() / 4);
			listDrawable.setBounds(bs);
		}
	}

	@Override
	public int getIntrinsicHeight() {
		return (type == FavoriteImageType.DRAWN) ? (synced ? syncedShadow.getHeight() : favBackground.getHeight()) : icon.getHeight();
	}

	@Override
	public int getIntrinsicWidth() {
		return (type == FavoriteImageType.DRAWN) ? (synced ? syncedShadow.getWidth() : favBackground.getWidth()) :  icon.getWidth();
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		paintBackground.setColorFilter(history ? grayFilter : colorFilter);
		Rect bs = getBounds();
		if (type == FavoriteImageType.DRAWN) {

			if (synced) {
				canvas.drawBitmap(syncedShadow, bs.exactCenterX() - syncedShadow.getWidth() / 2f, bs.exactCenterY() - syncedShadow.getHeight() / 2f, paintBackground);
				canvas.drawBitmap(syncedColor, bs.exactCenterX() - syncedColor.getWidth() / 2f, bs.exactCenterY() - syncedColor.getHeight() / 2f, paintBackground);
				canvas.drawBitmap(syncedStroke, bs.exactCenterX() - syncedStroke.getWidth() / 2f, bs.exactCenterY() - syncedStroke.getHeight() / 2f, paintBackground);
				canvas.drawBitmap(syncedIcon, bs.exactCenterX() - syncedIcon.getWidth() / 2f, bs.exactCenterY() - syncedIcon.getHeight() / 2f, paintIcon);
			} else if (withShadow) {
				canvas.drawBitmap(favBackground, bs.exactCenterX() - favBackground.getWidth() / 2f, bs.exactCenterY() - favBackground.getHeight() / 2f, paintBackground);
				canvas.drawBitmap(favIcon, bs.exactCenterX() - favIcon.getWidth() / 2f, bs.exactCenterY() - favIcon.getHeight() / 2f, paintIcon);
			} else {
				int min = Math.min(bs.width(), bs.height());
				int r = (min * 4 / 10);
				int rs = (r - 1);
				canvas.drawCircle(min / 2, min / 2, r, paintOuter);
				canvas.drawCircle(min / 2, min / 2, rs, paintInnerCircle);
				listDrawable.draw(canvas);
			}
		} else {
	            canvas.drawBitmap(icon, bs.exactCenterX() - icon.getWidth() / 2f, bs.exactCenterY() - icon.getHeight() / 2f, paintIcon);
		}
	}

	public void drawBitmapInCenter(Canvas canvas, float x, float y, boolean history) {
		this.history = history;

		float dx = x - getIntrinsicWidth() / 2f;
		float dy = y - getIntrinsicHeight() / 2f;
		canvas.translate(dx, dy);
		draw(canvas);
		canvas.translate(-dx, -dy);
	}


    @Override
	public int getOpacity() {
		return PixelFormat.UNKNOWN;
	}

	@Override
	public void setAlpha(int alpha) {
		paintBackground.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		paintIcon.setColorFilter(cf);
	}

	private static TreeMap<String, FavoriteImageDrawable> cache = new TreeMap<>();

	public static FavoriteImageDrawable getOrCreate(Context a, int color, String customIconFileName, boolean withShadow, boolean synced) {
		color = color | 0xff000000;
		String hash = (color << 2) + (withShadow ? 1 : 0) + (synced ? 3 : 0) + customIconFileName;
		FavoriteImageDrawable drawable = cache.get(hash);
		if (drawable == null) {
			drawable = new FavoriteImageDrawable(a, color, customIconFileName, withShadow, synced);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			cache.put(hash, drawable);
		}
		return drawable;
	}

	public static FavoriteImageDrawable getOrCreate(Context a, int color, String customIconFileName, boolean withShadow) {
		return getOrCreate(a, color, customIconFileName, withShadow, false);
	}

	public static FavoriteImageDrawable getOrCreateSyncedIcon(Context a, int color, String customIconFileName) {
		return getOrCreate(a, color, customIconFileName, false, true);
	}

	public static FavoriteImageDrawable getOrCreateSyncedIcon(Context a, int color) {
		return getOrCreate(a, color, "", false, true);
	}
}
