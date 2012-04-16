package br.unb.unbiquitous.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.listView.Item;
import br.unb.unbiquitous.listView.ItemListAdapter;

/**
 * Our main activity, show a list of items and allows to select some of them
 * 
 * @author marvinlabs
 */
public class ListViewActivity extends ListActivity implements OnItemClickListener {

	private List<Item> data;
	private ListView listView;
	private ItemListAdapter adapter;
	
	private HydraConnection hydraConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_check);
		
		data = new ArrayList<Item>();
		
		Intent intent = getIntent();
		
		ArrayList<String> drivers = intent.getStringArrayListExtra("drivers");
		
		for ( int i =0; i< drivers.size(); i++){
			
			
			
			data.add(	new Item(	Integer.valueOf(i).longValue(), 
									drivers.get(i), 
									hydraConnection.getDriverData(drivers.get(i))
								)
					);
		}
		
		Collections.sort(data, new Comparator<Item>() {
			public int compare(Item i1, Item i2) {
				return i1.getCaption().compareTo(i2.getCaption());
			}
		});

		// Create the adapter to render our data
		// --
		adapter = new ItemListAdapter(this, data);
		setListAdapter(adapter);

		// Get some views for later use
		// --
		listView = getListView();
		listView.setItemsCanFocus(false);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		for (int i = 0; i < listView.getCount(); ++i) {
			boolean isDriverInUse = hydraConnection.isDriverInUse(data.get(i).getDriverData());
			listView.setItemChecked(i, isDriverInUse);
		}
	}

	/**
	 * Desmarca todas as checkbox's.
	 */
	private void clearSelection() {
		final int itemCount = listView.getCount();
		for (int i = 0; i < itemCount; ++i) {
			listView.setItemChecked(i, false);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		boolean isItemMarcado = false;
		
		// Pega o item que foi selecionado.
		Item item = adapter.getItem(arg2);
		
		for(int i= 0; i < listView.getCheckItemIds().length; i++){
			if(item.getId() == listView.getCheckedItemIds().clone()[i]){
				isItemMarcado = true;
			}
		}
		
		// Não conectar se o driver selecionado for o próprio driver da hydra.
		if (item.getDriverData().equals(hydraConnection.getHydraApplication())){
			return;
		}
		
		// Redirecionar ou liberar recurso
		if(isItemMarcado){
			hydraConnection.redirectResource(item.getDriverData());
			item.setDriverInUse(true);
		}else{
			item.setDriverInUse(false);
			hydraConnection.releaseResource(item.getDriverData());
		}
		
		
		if (isItemMarcado){
			Toast.makeText(this, "Você marcou: " + item.getCaption(),Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "Você desmarcou: " + item.getCaption(),Toast.LENGTH_SHORT).show();
		}
		
	}

}