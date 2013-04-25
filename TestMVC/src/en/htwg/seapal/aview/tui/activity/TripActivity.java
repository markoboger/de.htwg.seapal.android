package en.htwg.seapal.aview.tui.activity;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import de.chritte.testmvc.R;
import en.htwg.seapal.controller.IBoatController;
import en.htwg.seapal.controller.ITripController;
import en.htwg.seapal.controller.impl.BoatController;
import en.htwg.seapal.controller.impl.TripController;
import en.htwg.seapal.observer.Event;
import en.htwg.seapal.observer.IObserver;

public class TripActivity extends Activity implements IObserver {

	private ITripController controller;
	private EditText in;
	private TextView out;
	private OnKeyListener onKeyListener;
	private UUID uuid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);

		controller = new TripController();
		controller.addObserver(this);

		in = (EditText) findViewById(R.id.input);
		out = (TextView) findViewById(R.id.output);
		printTUI();

		onKeyListener = listener();
		in.setOnKeyListener(onKeyListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void processInputLine() {

		

	}

	private String userInput(String message) {
		final String[] userInput = new String[1];
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Your Input");
		alert.setMessage(message);

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				userInput[0] = input.getText().toString();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();
		return userInput[0];
	}

	@Override
	public void update(Event e) {
		printTUI();
	}

	private void printTUI() {
		out.setText("l - List Trips\n" + "s - Set TripName\n" + "q - quit");
	}

	private OnKeyListener listener() {
		return new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				boolean handled = false;
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

					processInputLine();
					handled = true;
				}
				return handled;
			}
		};
	}

}