package net.osmand.plus.quickaction.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandSettings;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.quickaction.QuickAction;

public class NavAutoCenterMapAction extends QuickAction {

    public static final int TYPE = 28;

    public NavAutoCenterMapAction() {
        super(TYPE);
    }

    public NavAutoCenterMapAction(QuickAction quickAction) {
        super(quickAction);
    }

    private int previousAutoFollowRouteValue;

    @Override
    public void execute(MapActivity activity) {

        OsmandSettings settings = activity.getMyApplication().getSettings();

        if (settings.AUTO_FOLLOW_ROUTE.get() != 0)
        {
            previousAutoFollowRouteValue = settings.AUTO_FOLLOW_ROUTE.get();
            settings.AUTO_FOLLOW_ROUTE.set(0);
        }
        else
        {
            settings.AUTO_FOLLOW_ROUTE.set(previousAutoFollowRouteValue);
        }

        Toast.makeText(activity, activity.getString(settings.AUTO_FOLLOW_ROUTE.get() == 0
                ? R.string.quick_action_auto_center_off : R.string.quick_action_auto_center_on), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void drawUI(ViewGroup parent, MapActivity activity) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quick_action_with_text, parent, false);

        ((TextView) view.findViewById(R.id.text)).setText(
                R.string.quick_action_auto_center_desc);

        parent.addView(view);
    }

    @Override
    public String getActionText(OsmandApplication application) {

        return (application.getSettings().AUTO_FOLLOW_ROUTE.get() > 0)
                ? application.getString(R.string.quick_action_auto_center_off) : application.getString(R.string.quick_action_auto_center_on);
    }

    @Override
    public boolean isActionWithSlash(OsmandApplication application) {

        return application.getSettings().AUTO_FOLLOW_ROUTE.get() > 0;
    }
}
