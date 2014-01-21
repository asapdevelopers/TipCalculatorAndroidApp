package com.asap.tipcalculator;

import com.asap.tipcalculator.R;
import com.asap.tipcalculator.dataaccess.TipsDataSource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.asap.tipcalculator.MESSAGE";
	private TipsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Intent intent = getIntent();
		String taxAsString = intent
				.getStringExtra(MainActivity.EXTRA_TAX_MESSAGE);
		double tax = Double.parseDouble(taxAsString);
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxTax);
		EditText taxET = (EditText) findViewById(R.id.tax_value);
		check.setChecked(tax != 0);
		taxET.setEnabled(tax != 0);
		if (tax != 0) {
			taxET.setText("" + tax);
		}
		datasource = new TipsDataSource(this);
		datasource.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}

	public void goBack(View view) {
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxTax);
		Intent intent = new Intent();
		EditText tax = (EditText) findViewById(R.id.tax_value);
		String message;
		if (check.isChecked()) {
			message = tax.getText().toString();
		} else {
			message = "0";
		}
		if (datasource.getTaxCount() > 0) {
			datasource.updateTax(message);
		} else {
			datasource.createTax(message);
		}
		intent.putExtra(EXTRA_MESSAGE, message);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	public void goToAsap(View view) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse("http://www.asapmobility.com"));
		startActivity(intent);
	}

	public void enableTax(View view) {
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxTax);
		EditText taxEditText = (EditText) findViewById(R.id.tax_value);
		taxEditText.setEnabled(check.isChecked());
	}

}
