package icaksama.com.icaksamacrud;

import icaksama.com.icaksamacrud.util.CustomHttpClient;
import icaksama.com.icaksamacrud.util.ListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CrudSelect extends ListActivity {

	//Array untuk menampung data yang di ambil dari MySQL
	private String[] kdSelect;
	private String[] Nama;
	private String[] deskripsi;
	private String[] Gambar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Mengeksekusi kelas GetData untuk mengirim permintaan ke MySQL
		new GetData()
				.execute("http://192.168.1.13/nongkinongki/android/getdata.php");
	}

	//Method untuk mengeluarkan event saat list di click
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getBaseContext(), "Terpilih " + Nama[position],
				Toast.LENGTH_LONG).show();
	}

	//Class GetData yang menuruni kelas AsyncTask untuk melakukan requset data dari internet
	private class GetData extends AsyncTask<String, Void, String> {

		// Instansiasi class dialog
		ProgressDialog dialog = new ProgressDialog(CrudSelect.this);
		String Content;
		String Error = null;
		// membuat object class JSONObject yang digunakan untuk menangkap data
		// dengan format json
		JSONObject jObject;
		// instansiasi class ArrayList
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		@Override
		protected String doInBackground(String... params) {
			try {
				Content = CustomHttpClient.executeHttpPost(
						"http://192.168.1.13/nongkinongki/android/getdata.php",
						data);
			} catch (ClientProtocolException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Content;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// menampilkan dialog pada saat proses pengambilan data dari
			// internet
			this.dialog.setMessage("Loading Data..");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// menutup dialog saat pengambilan data selesai
			this.dialog.dismiss();
			if (Error != null) {
				Toast.makeText(getBaseContext(), "Error Connection Internet",
						Toast.LENGTH_LONG).show();
			} else {
				try {
					// instansiasi kelas JSONObject
					jObject = new JSONObject(Content);
					// mengubah json dalam bentuk array
					JSONArray menuitemArray = jObject.getJSONArray("select");

					// mendeskripsikan jumlah array yang bisa di tampung
					kdSelect = new String[menuitemArray.length()];
					Nama = new String[menuitemArray.length()];
					deskripsi = new String[menuitemArray.length()];
					Gambar = new String[menuitemArray.length()];

					// mengisi variable array dengan data yang di ambil dari
					// internet yang telah dibuah menjadi Array
					for (int i = 0; i < menuitemArray.length(); i++) {
						kdSelect[i] = menuitemArray.getJSONObject(i)
								.getString("kdselect").toString();
						Nama[i] = menuitemArray.getJSONObject(i)
								.getString("nama").toString();
						deskripsi[i] = menuitemArray.getJSONObject(i)
								.getString("deskripsi").toString();
						Gambar[i] = "http://192.168.1.13/nongkinongki/gambar/"
								+ menuitemArray.getJSONObject(i)
										.getString("gambar").toString();
					}
					// instansiasi class ListAdapter (Buka class ListAdapter)
					ListAdapter adapter = new ListAdapter(getBaseContext(),
							Nama, deskripsi, Gambar);
					setListAdapter(adapter);
				} catch (JSONException ex) {
					Logger.getLogger(CrudSelect.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}
	}

}
