package en.htwg.seapal.aview.tui.states.map;

import android.content.Context;
import android.content.Intent;
import en.htwg.seapal.aview.tui.StateContext;
import en.htwg.seapal.aview.tui.TuiState;
import en.htwg.seapal.aview.tui.activity.BoatActivity;
import en.htwg.seapal.aview.tui.states.boat.ShowState;

public class StartState implements TuiState {

	@Override
	public String buildString(StateContext context) {
		return "l  -  logbuch\n" + "d  -  Dashboard\n" + "m  -  marks\n"
				+ "r  -  routes";
	}

	@Override
	public boolean process(StateContext context, String input) {
		Intent intent = null;
		switch (input.charAt(0)) {
		case 'l':
			intent = new Intent((Context) context, BoatActivity.class);
			((Context) context).startActivity(intent);
			break;
		default:
			return false;
		}
		return true;
	}

}
