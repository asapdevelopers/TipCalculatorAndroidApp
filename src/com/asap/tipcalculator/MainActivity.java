package com.asap.tipcalculator;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.asap.tipcalculator.dataaccess.Tip;
import com.asap.tipcalculator.dataaccess.TipsDataSource;

public class MainActivity extends Activity {

	private TipsDataSource datasource;
	private double tax;
	private EditText billEditText;
	public final static String EXTRA_TAX_MESSAGE = "com.example.tipcalculatortest.TAX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		billEditText = (EditText) findViewById(R.id.edit_message);

		billEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					TextView tipPerc = (TextView) findViewById(R.id.tip_percentage);
					String percentageAsText = tipPerc.getText().toString()
							.split("%")[0].trim();
					int percentageAsInt = Integer.parseInt(percentageAsText);
					TextView share = (TextView) findViewById(R.id.sharedBy_value);

					int shareAsInt = Integer.parseInt(share.getText()
							.toString().trim());
					calculateTotal(percentageAsInt);
					calculateShare(shareAsInt);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		initialize();

		datasource = new TipsDataSource(this);
		datasource.open();

		if (datasource.getTaxCount() > 0)
			tax = Double.parseDouble(datasource.getLastTax().getTax());
		Tip tip = datasource.getLastTip();
		TextView tipPerc = (TextView) findViewById(R.id.tip_percentage);
		if (tip != null) {
			tipPerc.setText(" " + tip.getTip());
		} else {
			tipPerc.setText(" 10 %");
		}
		Intent intent = getIntent();
		String message = intent.getStringExtra(SettingsActivity.EXTRA_MESSAGE);
		if (message != null)
			tax = Integer.parseInt(message);
	}

	private void initialize() {
		TextView tipCalc = (TextView) findViewById(R.id.tip_calculation);
		tipCalc.setText(" $ 0 ");
		TextView share = (TextView) findViewById(R.id.sharedBy_value);
		share.setText(" 1");
		TextView perPerson = (TextView) findViewById(R.id.perPerson_Value);
		perPerson.setText(" $ 0");
		TextView total = (TextView) findViewById(R.id.total_Value);
		total.setText(" $ 0");
	}

	protected void calculateTotal(int percentageAsInt) {
		try {
			String billAsText = billEditText.getText().toString().trim();
			billAsText = billAsText.replace(',', '.');
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			Number number = format.parse(billAsText);
			double bill = number.doubleValue();

			double tipValue = bill * percentageAsInt / 100;
			TextView tipCalc = (TextView) findViewById(R.id.tip_calculation);
			tipCalc.setText(" $ " + String.format("%.2f", tipValue));
			TextView total = (TextView) findViewById(R.id.total_Value);
			double totalValue = (bill + tipValue) * (1 + tax / 100);
			total.setText("$ " + String.format("%.2f", totalValue));
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		TextView tipPerc = (TextView) findViewById(R.id.tip_percentage);
		long count = datasource.getTipsCount();
		if (count > 0) {
			datasource.updateTip(tipPerc.getText().toString().trim());
		} else {
			datasource.createTip(tipPerc.getText().toString().trim());
		}
		datasource.close();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void increaseTip(View view) {
		TextView tipPerc = (TextView) findViewById(R.id.tip_percentage);
		String percentageAsText = tipPerc.getText().toString().split("%")[0]
				.trim();
		int percentageAsInt = Integer.parseInt(percentageAsText);
		if (percentageAsInt < 100)
			percentageAsInt++;
		tipPerc.setText(" " + percentageAsInt + " %");
		try {
			calculateTotal(percentageAsInt);
			TextView share = (TextView) findViewById(R.id.sharedBy_value);
			int shareAsInt = Integer
					.parseInt(share.getText().toString().trim());
			calculateShare(shareAsInt);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void decreaseTip(View view) {
		TextView tipPerc = (TextView) findViewById(R.id.tip_percentage);
		String percentageAsText = tipPerc.getText().toString().split("%")[0]
				.trim();
		int percentageAsInt = Integer.parseInt(percentageAsText);
		if (percentageAsInt > 0)
			percentageAsInt--;
		tipPerc.setText(" " + percentageAsInt + " %");
		try {
			calculateTotal(percentageAsInt);
			TextView share = (TextView) findViewById(R.id.sharedBy_value);
			int shareAsInt = Integer
					.parseInt(share.getText().toString().trim());
			calculateShare(shareAsInt);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void decreaseShare(View view) {
		TextView share = (TextView) findViewById(R.id.sharedBy_value);

		int shareAsInt = Integer.parseInt(share.getText().toString().trim());
		if (shareAsInt > 1)
			shareAsInt--;
		share.setText(" " + shareAsInt);
		try {
			calculateShare(shareAsInt);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void calculateShare(int shareAsInt) {
		try {
			TextView total = (TextView) findViewById(R.id.total_Value);
			String totalAsText = total.getText().toString().split("\\$")[1]
					.trim();
			totalAsText = totalAsText.replace(',', '.');
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			Number number = format.parse(totalAsText);
			double totalAsDouble = number.doubleValue();
			double sharePerPerson = totalAsDouble / shareAsInt;
			TextView perPerson = (TextView) findViewById(R.id.perPerson_Value);
			perPerson.setText(" $ " + String.format("%.2f", sharePerPerson));
		} catch (Exception e) {
		}
	}

	public void increaseShare(View view) {
		TextView share = (TextView) findViewById(R.id.sharedBy_value);

		int shareAsInt = Integer.parseInt(share.getText().toString().trim());
		shareAsInt++;
		share.setText(" " + shareAsInt);
		try {
			calculateShare(shareAsInt);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void goToSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra(EXTRA_TAX_MESSAGE, tax + "");
		startActivityForResult(intent, 0);
		// startActivity(intent);
	}

	public void clearAll(View view) {
		clearAll();
	}

	private void clearAll() {
		TextView tipCalc = (TextView) findViewById(R.id.tip_calculation);
		tipCalc.setText(" $ 0 ");
		TextView share = (TextView) findViewById(R.id.sharedBy_value);
		share.setText(" 1");
		TextView perPerson = (TextView) findViewById(R.id.perPerson_Value);
		perPerson.setText(" $ 0");
		TextView total = (TextView) findViewById(R.id.total_Value);
		total.setText(" $ 0");
		billEditText.setText("");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String message = data
					.getStringExtra(SettingsActivity.EXTRA_MESSAGE);
			tax = Double.parseDouble(message);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret;
		if (item.getItemId() == R.id.menu_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(EXTRA_TAX_MESSAGE, tax + "");
			startActivityForResult(intent, 0);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	public void roundUp(View view) {
		try {
			TextView total = (TextView) findViewById(R.id.total_Value);
			String totalAsString = total.getText().toString();
			totalAsString = totalAsString.replace(',', '.');
			String[] parts = totalAsString.split("\\$");
			NumberFormat format = NumberFormat.getInstance(Locale.US);
			Number number = format.parse(parts[1].trim());
			double totalAsDouble = number.doubleValue();
			double roundedTotal = Math.ceil(totalAsDouble);
			total.setText("$ " + roundedTotal);
		} catch (Exception e) {
		}
	}
}
