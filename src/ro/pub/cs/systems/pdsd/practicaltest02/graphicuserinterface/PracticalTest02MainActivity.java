package ro.pub.cs.systems.pdsd.practicaltest02.graphicuserinterface;

import java.net.ServerSocket;

import ro.pub.cs.systems.pdsd.practicaltest02.R;
import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.networkingthreads.ClientThread;
import ro.pub.cs.systems.pdsd.practicaltest02.networkingthreads.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends Activity {
	
	private EditText op1EditText, op2EditText;
	private EditText addEditText, mulEditText, portEditText;
	private Button plusButton, mulButton;
	
	//private ServerSocket serverSocket;
	private ServerThread serverThread;
	
	private int server_port;
	
	private OpButtonListener opButtonListener = new OpButtonListener();
	private class OpButtonListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String query;
			TextView answer_view;
			if (view == plusButton) {
				query = "add,";
				answer_view = addEditText;
			} else {
				query = "mul,";
				answer_view = mulEditText;
			}
			answer_view.setText("");
			query += op1EditText.getText().toString() + "," + op2EditText.getText().toString();
			
			ClientThread clientThread = new ClientThread("localhost", server_port, query, answer_view);
			clientThread.start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_main);
		
		op1EditText = (EditText) findViewById(R.id.op1editText);
		op2EditText = (EditText) findViewById(R.id.op2editText);
		
		addEditText = (EditText) findViewById(R.id.plusEditText);
		mulEditText = (EditText) findViewById(R.id.mulEditText);
		
		portEditText = (EditText) findViewById(R.id.portEditText);
		
		plusButton = (Button) findViewById(R.id.plusButton);
		mulButton = (Button) findViewById(R.id.mulButton);
		
		plusButton.setOnClickListener(opButtonListener);
		mulButton.setOnClickListener(opButtonListener);
		
		serverThread = new ServerThread(0);
		if (serverThread.getServerSocket() != null) {
			serverThread.start();
			server_port = serverThread.getServerSocket().getLocalPort();
			portEditText.setText(server_port + "");
			
		} else {
			Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
		}
		
	}
	
	@Override
	protected void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
