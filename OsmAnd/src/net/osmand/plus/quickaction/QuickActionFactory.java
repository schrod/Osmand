package net.osmand.plus.quickaction;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.R;
import net.osmand.plus.audionotes.AudioVideoNotesPlugin;
import net.osmand.plus.audionotes.TakeAudioNoteAction;
import net.osmand.plus.audionotes.TakePhotoNoteAction;
import net.osmand.plus.audionotes.TakeVideoNoteAction;
import net.osmand.plus.osmedit.OsmEditingPlugin;
import net.osmand.plus.parkingpoint.ParkingAction;
import net.osmand.plus.parkingpoint.ParkingPositionPlugin;
import net.osmand.plus.quickaction.actions.AddOSMBugAction;
import net.osmand.plus.quickaction.actions.AddPOIAction;
import net.osmand.plus.quickaction.actions.FavoriteAction;
import net.osmand.plus.quickaction.actions.GPXAction;
import net.osmand.plus.quickaction.actions.MapOverlayAction;
import net.osmand.plus.quickaction.actions.MapSourceAction;
import net.osmand.plus.quickaction.actions.MapStyleAction;
import net.osmand.plus.quickaction.actions.MapUnderlayAction;
import net.osmand.plus.quickaction.actions.MarkerAction;
import net.osmand.plus.quickaction.actions.NavAddDestinationAction;
import net.osmand.plus.quickaction.actions.NavAddFirstIntermediateAction;
import net.osmand.plus.quickaction.actions.NavAutoZoomMapAction;
import net.osmand.plus.quickaction.actions.NavAutoCenterMapAction;
import net.osmand.plus.quickaction.actions.NavReplaceDestinationAction;
import net.osmand.plus.quickaction.actions.NavResumePauseAction;
import net.osmand.plus.quickaction.actions.NavSkipFirstIntermediateAction;
import net.osmand.plus.quickaction.actions.NavStartStopAction;
import net.osmand.plus.quickaction.actions.NavVoiceAction;
import net.osmand.plus.quickaction.actions.NewAction;
import net.osmand.plus.quickaction.actions.ShowHideFavoritesAction;
import net.osmand.plus.quickaction.actions.ShowHideGpxTracksAction;
import net.osmand.plus.quickaction.actions.ShowHideOSMBugAction;
import net.osmand.plus.quickaction.actions.ShowHidePoiAction;
import net.osmand.plus.quickaction.actions.DayNightModeAction;
import net.osmand.plus.rastermaps.OsmandRasterMapsPlugin;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuickActionFactory {

	public String quickActionListToString(List<QuickAction> quickActions) {
		return new Gson().toJson(quickActions);
	}

	public List<QuickAction> parseActiveActionsList(String json) {
		Type type = new TypeToken<List<QuickAction>>() {
		}.getType();
		ArrayList<QuickAction> quickActions = new Gson().fromJson(json, type);
		return quickActions != null ? quickActions : new ArrayList<QuickAction>();
	}

	public static List<QuickAction> produceTypeActionsListWithHeaders(List<QuickAction> active) {
		ArrayList<QuickAction> quickActions = new ArrayList<>();
		quickActions.add(new QuickAction(QuickAction.QuickActionType.QUICK_ACTION_HEADER, R.string.quick_action_add_create_items));
		quickActions.add(new FavoriteAction());
		quickActions.add(new GPXAction());
		QuickAction marker = new MarkerAction();

		if (!marker.hasInstanceInList(active)) {
			quickActions.add(marker);
		}

		if (OsmandPlugin.getEnabledPlugin(AudioVideoNotesPlugin.class) != null) {
			QuickAction audio = new TakeAudioNoteAction();
			QuickAction photo = new TakePhotoNoteAction();
			QuickAction video = new TakeVideoNoteAction();

			if (!audio.hasInstanceInList(active)) {
				quickActions.add(audio);
			}

			if (!photo.hasInstanceInList(active)) {
				quickActions.add(photo);
			}

			if (!video.hasInstanceInList(active)) {
				quickActions.add(video);
			}
		}

		if (OsmandPlugin.getEnabledPlugin(OsmEditingPlugin.class) != null) {
			quickActions.add(new AddPOIAction());
			quickActions.add(new AddOSMBugAction());
		}

		if (OsmandPlugin.getEnabledPlugin(ParkingPositionPlugin.class) != null) {
			QuickAction parking = new ParkingAction();
			if (!parking.hasInstanceInList(active)) {
				quickActions.add(parking);
			}
		}

		quickActions.add(new QuickAction(QuickAction.QuickActionType.QUICK_ACTION_HEADER, R.string.quick_action_add_configure_map));

		QuickAction favorites = new ShowHideFavoritesAction();
		if (!favorites.hasInstanceInList(active)) {
			quickActions.add(favorites);
		}

		quickActions.add(new ShowHideGpxTracksAction());

		quickActions.add(new ShowHidePoiAction());
		if (OsmandPlugin.getEnabledPlugin(OsmEditingPlugin.class) != null) {
			QuickAction showHideOSMBugAction = new ShowHideOSMBugAction();
			if (!showHideOSMBugAction.hasInstanceInList(active)) {
				quickActions.add(showHideOSMBugAction);
			}
		}

		quickActions.add(new MapStyleAction());
		if (OsmandPlugin.getEnabledPlugin(OsmandRasterMapsPlugin.class) != null) {
			quickActions.add(new MapSourceAction());
			quickActions.add(new MapOverlayAction());
			quickActions.add(new MapUnderlayAction());
		}

		quickActions.add(new DayNightModeAction());


		QuickAction voice = new NavVoiceAction();
		QuickAction addDestination = new NavAddDestinationAction();
		QuickAction addFirstIntermediate = new NavAddFirstIntermediateAction();
		QuickAction skipFirstIntermediate = new NavSkipFirstIntermediateAction();
		QuickAction replaceDestination = new NavReplaceDestinationAction();
		QuickAction autoZoomMap = new NavAutoZoomMapAction();
		QuickAction autoCenterMap = new NavAutoCenterMapAction();
		QuickAction startStopNavigation = new NavStartStopAction();
		QuickAction resumePauseNavigation = new NavResumePauseAction();

		ArrayList<QuickAction> navigationQuickActions = new ArrayList<>();

		if (!voice.hasInstanceInList(active)) {
			navigationQuickActions.add(voice);
		}
		if (!addDestination.hasInstanceInList(active)) {
			navigationQuickActions.add(addDestination);
		}
		if (!addFirstIntermediate.hasInstanceInList(active)) {
			navigationQuickActions.add(addFirstIntermediate);
		}
		if (!skipFirstIntermediate.hasInstanceInList(active)) {
			navigationQuickActions.add(skipFirstIntermediate);
		}
		if (!replaceDestination.hasInstanceInList(active)) {
			navigationQuickActions.add(replaceDestination);
		}
		if (!autoZoomMap.hasInstanceInList(active)) {
			navigationQuickActions.add(autoZoomMap);
		}
		if (!autoCenterMap.hasInstanceInList(active)) {
			navigationQuickActions.add(autoCenterMap);
		}
		if (!startStopNavigation.hasInstanceInList(active)) {
			navigationQuickActions.add(startStopNavigation);
		}
		if (!resumePauseNavigation.hasInstanceInList(active)) {
			navigationQuickActions.add(resumePauseNavigation);
		}

		if (navigationQuickActions.size() > 0) {
			quickActions.add(new QuickAction(QuickAction.QuickActionType.QUICK_ACTION_HEADER, R.string.quick_action_add_navigation));
			quickActions.addAll(navigationQuickActions);
		}

		return quickActions;
	}

	public static QuickAction newActionByType(QuickAction.QuickActionType type) {

		switch (type) {

			case NEW_ACTION:
				return new NewAction();

			case MARKER_ACTION:
				return new MarkerAction();

			case FAVORITE_ACTION:
				return new FavoriteAction();

			case SHOW_HIDE_FAVORITES_ACTION:
				return new ShowHideFavoritesAction();

			case SHOW_HIDE_POI_ACTION:
				return new ShowHidePoiAction();

			case GPX_ACTION:
				return new GPXAction();

			case PARKING_ACTION:
				return new ParkingAction();

			case TAKE_AUDIO_NOTE_ACTION:
				return new TakeAudioNoteAction();

			case TAKE_PHOTO_NOTE_ACTION:
				return new TakePhotoNoteAction();

			case TAKE_VIDEO_NOTE_ACTION:
				return new TakeVideoNoteAction();

			case NAV_VOICE_ACTION:
				return new NavVoiceAction();

			case SHOW_HIDE_OSM_BUG_ACTION:
				return new ShowHideOSMBugAction();

			case ADD_OSM_BUG_ACTION:
				return new AddOSMBugAction();

			case ADD_POI_ACTION:
				return new AddPOIAction();

			case MAP_STYLE_ACTION:
				return new MapStyleAction();

			case MAP_SOURCE_ACTION:
				return new MapSourceAction();

			case MAP_OVERLAY_ACTION:
				return new MapOverlayAction();

			case MAP_UNDERLAY_ACTION:
				return new MapUnderlayAction();

			case NAV_ADD_DESTINATION_ACTION:
				return new NavAddDestinationAction();

			case NAV_ADD_FIRST_INTERMEDIATE_ACTION:
				return new NavAddFirstIntermediateAction();

			case NAV_REPLACE_DESTINATION_ACTION:
				return new NavReplaceDestinationAction();

			case NAV_AUTO_ZOOM_MAP_ACTION:
				return new NavAutoZoomMapAction();

			case NAV_AUTO_CENTER_MAP_ACTION:
				return new NavAutoCenterMapAction();

			case NAV_START_STOP_ACTION:
				return new NavStartStopAction();

			case NAV_RESUME_PAUSE_ACTION:
				return new NavResumePauseAction();

			case DAY_NIGHT_MODE_ACTION:
				return new DayNightModeAction();
				
			case NAV_SKIP_FIRST_INTERMEDIATE_ACTION:
				return new NavSkipFirstIntermediateAction();

			case SHOW_HIDE_GPX_TRACKS_ACTION:
				return new ShowHideGpxTracksAction();

			default:
				return new QuickAction();
		}
	}

	public static QuickAction produceAction(QuickAction quickAction) {

		switch (quickAction.type) {

			case NEW_ACTION:
				return new NewAction(quickAction);

			case MARKER_ACTION:
				return new MarkerAction(quickAction);

			case FAVORITE_ACTION:
				return new FavoriteAction(quickAction);

			case SHOW_HIDE_FAVORITES_ACTION:
				return new ShowHideFavoritesAction(quickAction);

			case SHOW_HIDE_POI_ACTION:
				return new ShowHidePoiAction(quickAction);

			case GPX_ACTION:
				return new GPXAction(quickAction);

			case PARKING_ACTION:
				return new ParkingAction(quickAction);

			case TAKE_AUDIO_NOTE_ACTION:
				return new TakeAudioNoteAction(quickAction);

			case TAKE_PHOTO_NOTE_ACTION:
				return new TakePhotoNoteAction(quickAction);

			case TAKE_VIDEO_NOTE_ACTION:
				return new TakeVideoNoteAction(quickAction);

			case NAV_VOICE_ACTION:
				return new NavVoiceAction(quickAction);

			case SHOW_HIDE_OSM_BUG_ACTION:
				return new ShowHideOSMBugAction(quickAction);

			case ADD_OSM_BUG_ACTION:
				return new AddOSMBugAction(quickAction);

			case ADD_POI_ACTION:
				return new AddPOIAction(quickAction);

			case MAP_STYLE_ACTION:
				return new MapStyleAction(quickAction);

			case MAP_SOURCE_ACTION:
				return new MapSourceAction(quickAction);

			case MAP_OVERLAY_ACTION:
				return new MapOverlayAction(quickAction);

			case MAP_UNDERLAY_ACTION:
				return new MapUnderlayAction(quickAction);

			case NAV_ADD_DESTINATION_ACTION:
				return new NavAddDestinationAction(quickAction);

			case NAV_ADD_FIRST_INTERMEDIATE_ACTION:
				return new NavAddFirstIntermediateAction(quickAction);

			case NAV_REPLACE_DESTINATION_ACTION:
				return new NavReplaceDestinationAction(quickAction);

			case NAV_AUTO_ZOOM_MAP_ACTION:
				return new NavAutoZoomMapAction(quickAction);

			case NAV_AUTO_CENTER_MAP_ACTION:
				return new NavAutoCenterMapAction(quickAction);

			case NAV_START_STOP_ACTION:
				return new NavStartStopAction(quickAction);

			case NAV_RESUME_PAUSE_ACTION:
				return new NavResumePauseAction(quickAction);

			case DAY_NIGHT_MODE_ACTION:
				return new DayNightModeAction(quickAction);
				
			case NAV_SKIP_FIRST_INTERMEDIATE_ACTION:
				return new NavSkipFirstIntermediateAction(quickAction);

			case SHOW_HIDE_GPX_TRACKS_ACTION:
				return new ShowHideGpxTracksAction(quickAction);

			default:
				return quickAction;
		}
	}

	public static @DrawableRes int getActionIcon(QuickAction.QuickActionType type) {

		switch (type) {

			case NEW_ACTION:
				return R.drawable.ic_action_plus;

			case MARKER_ACTION:
				return R.drawable.ic_action_flag_dark;

			case FAVORITE_ACTION:
				return R.drawable.ic_action_fav_dark;

			case SHOW_HIDE_FAVORITES_ACTION:
				return R.drawable.ic_action_fav_dark;

			case SHOW_HIDE_POI_ACTION:
				return R.drawable.ic_action_gabout_dark;

			case GPX_ACTION:
				return R.drawable.ic_action_flag_dark;

			case PARKING_ACTION:
				return R.drawable.ic_action_parking_dark;

			case TAKE_AUDIO_NOTE_ACTION:
				return R.drawable.ic_action_micro_dark;

			case TAKE_PHOTO_NOTE_ACTION:
				return R.drawable.ic_action_photo_dark;

			case TAKE_VIDEO_NOTE_ACTION:
				return R.drawable.ic_action_video_dark;

			case NAV_VOICE_ACTION:
				return R.drawable.ic_action_volume_up;

			case SHOW_HIDE_OSM_BUG_ACTION:
				return R.drawable.ic_action_bug_dark;

			case ADD_OSM_BUG_ACTION:
				return R.drawable.ic_action_bug_dark;

			case ADD_POI_ACTION:
				return R.drawable.ic_action_gabout_dark;

			case MAP_STYLE_ACTION:
				return R.drawable.ic_map;

			case MAP_SOURCE_ACTION:
				return R.drawable.ic_world_globe_dark;

			case MAP_OVERLAY_ACTION:
				return R.drawable.ic_layer_top_dark;

			case MAP_UNDERLAY_ACTION:
				return R.drawable.ic_layer_bottom_dark;

			case NAV_ADD_DESTINATION_ACTION:
				return R.drawable.ic_action_point_add_destination;

			case NAV_ADD_FIRST_INTERMEDIATE_ACTION:
				return R.drawable.ic_action_intermediate;

			case NAV_REPLACE_DESTINATION_ACTION:
				return R.drawable.ic_action_point_add_destination;

			case NAV_AUTO_ZOOM_MAP_ACTION:
				return R.drawable.ic_action_search_dark;

			case NAV_AUTO_CENTER_MAP_ACTION:
				return R.drawable.ic_action_search_dark;

			case NAV_START_STOP_ACTION:
				return R.drawable.ic_action_start_navigation;

			case NAV_RESUME_PAUSE_ACTION:
				return R.drawable.ic_play_dark;

			case DAY_NIGHT_MODE_ACTION:
				return R.drawable.ic_action_map_day;
				
			case NAV_SKIP_FIRST_INTERMEDIATE_ACTION:
				return R.drawable.ic_action_intermediate;

			case SHOW_HIDE_GPX_TRACKS_ACTION:
				return R.drawable.ic_gpx_track;

			default:
				return R.drawable.ic_action_plus;
		}
	}

	public static @StringRes int getActionName(QuickAction.QuickActionType type) {

		switch (type) {

			case NEW_ACTION:
				return R.string.quick_action_new_action;

			case MARKER_ACTION:
				return R.string.quick_action_add_marker;

			case FAVORITE_ACTION:
				return R.string.quick_action_add_favorite;

			case SHOW_HIDE_FAVORITES_ACTION:
				return R.string.quick_action_showhide_favorites_title;

			case SHOW_HIDE_POI_ACTION:
				return R.string.quick_action_showhide_poi_title;

			case GPX_ACTION:
				return R.string.quick_action_add_gpx;

			case PARKING_ACTION:
				return R.string.quick_action_add_parking;

			case TAKE_AUDIO_NOTE_ACTION:
				return R.string.quick_action_take_audio_note;

			case TAKE_PHOTO_NOTE_ACTION:
				return R.string.quick_action_take_photo_note;

			case TAKE_VIDEO_NOTE_ACTION:
				return R.string.quick_action_take_video_note;

			case NAV_VOICE_ACTION:
				return R.string.quick_action_navigation_voice;

			case SHOW_HIDE_OSM_BUG_ACTION:
				return R.string.quick_action_showhide_osmbugs_title;

			case ADD_OSM_BUG_ACTION:
				return R.string.quick_action_add_osm_bug;

			case ADD_POI_ACTION:
				return R.string.quick_action_add_poi;

			case MAP_STYLE_ACTION:
				return R.string.quick_action_map_style;

			case MAP_SOURCE_ACTION:
				return R.string.quick_action_map_source;

			case MAP_OVERLAY_ACTION:
				return R.string.quick_action_map_overlay;

			case MAP_UNDERLAY_ACTION:
				return R.string.quick_action_map_underlay;

			case DAY_NIGHT_MODE_ACTION:
				return R.string.quick_action_day_night_switch_mode;

			case NAV_ADD_DESTINATION_ACTION:
				return R.string.quick_action_add_destination;

			case NAV_ADD_FIRST_INTERMEDIATE_ACTION:
				return R.string.quick_action_add_first_intermediate;

			case NAV_REPLACE_DESTINATION_ACTION:
				return R.string.quick_action_replace_destination;

			case NAV_AUTO_ZOOM_MAP_ACTION:
				return R.string.quick_action_auto_zoom;

			case NAV_AUTO_CENTER_MAP_ACTION:
				return R.string.quick_action_auto_center;

			case NAV_START_STOP_ACTION:
				return R.string.quick_action_start_stop_navigation;

			case NAV_RESUME_PAUSE_ACTION:
				return R.string.quick_action_resume_pause_navigation;

			case SHOW_HIDE_GPX_TRACKS_ACTION:
				return R.string.quick_action_show_hide_gpx_tracks;

			case NAV_SKIP_FIRST_INTERMEDIATE_ACTION:
				return R.string.quick_action_skip_first_intermediate;

			default:
				return R.string.quick_action_new_action;
		}
	}

	public static boolean isActionEditable(QuickAction.QuickActionType type) {

		switch (type) {
			case NEW_ACTION:
			case MARKER_ACTION:
			case SHOW_HIDE_FAVORITES_ACTION:
			case SHOW_HIDE_POI_ACTION:
			case PARKING_ACTION:
			case TAKE_AUDIO_NOTE_ACTION:
			case TAKE_PHOTO_NOTE_ACTION:
			case TAKE_VIDEO_NOTE_ACTION:
			case NAV_VOICE_ACTION:
			case NAV_ADD_DESTINATION_ACTION:
			case NAV_ADD_FIRST_INTERMEDIATE_ACTION:
			case NAV_REPLACE_DESTINATION_ACTION:
			case NAV_AUTO_ZOOM_MAP_ACTION:
			case NAV_AUTO_CENTER_MAP_ACTION:
			case SHOW_HIDE_OSM_BUG_ACTION:
			case NAV_START_STOP_ACTION:
			case NAV_RESUME_PAUSE_ACTION:
			case DAY_NIGHT_MODE_ACTION:
			case SHOW_HIDE_GPX_TRACKS_ACTION:
				return false;

			default:
				return true;
		}
	}
}
