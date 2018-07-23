package net.osmand.plus.quickaction.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.osmand.data.LatLon;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.quickaction.QuickAction;
import net.osmand.plus.routing.RoutingHelper;

public class NavSkipFirstIntermediateAction extends QuickAction {

	public static final QuickActionType TYPE = QuickActionType.NAV_SKIP_FIRST_INTERMEDIATE_ACTION;

	public NavSkipFirstIntermediateAction() {
		super(TYPE);
	}

	public NavSkipFirstIntermediateAction(QuickAction quickAction) {
		super(quickAction);
	}

	@Override
	public void execute(MapActivity activity) {
		RoutingHelper routingHelper = activity.getRoutingHelper();
		routingHelper.skipNextIntermediate();

	}

	@Override
	public void drawUI(ViewGroup parent, MapActivity activity) {

		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.quick_action_with_text, parent, false);

		((TextView) view.findViewById(R.id.text)).setText(
				R.string.quick_action_skip_first_intermediate_desc);

		parent.addView(view);
	}
}
