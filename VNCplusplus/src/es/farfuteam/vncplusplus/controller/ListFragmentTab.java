/*
 	Copyright 2013 Oscar Crespo Salazar
 	Copyright 2013 Gorka Jimeno Garrachon
 	Copyright 2013 Luis Valero Martin
  
	This file is part of VNC++.

	VNC++ is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	any later version.
	
	VNC++ is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with VNC++.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.farfuteam.vncplusplus.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import es.farfuteam.vncplusplus.model.sql.Connection;
import es.farfuteam.vncplusplus.model.sql.ConnectionSQLite;
import es.farfuteam.vncplusplus.view.Adapter;
import es.farfuteam.vncplusplus.view.DialogOptions;
import es.farfuteam.vncplusplus.view.DialogOptions.SuperListener;
import es.farfuteam.vncplusplus.controller.R;

public class ListFragmentTab extends SherlockListFragment implements SuperListener{
	
	
	private static Object o;
	
	private static Adapter adapter;
	private ArrayList<Connection> userList;
	private static ConnectionSQLite admin;
	private View view;
	
	private LayoutInflater inflater;
	private ViewGroup container;
	private Bundle savedInstanceState;
	
	private static ListFragmentTab instance = null;
	   
	public ListFragmentTab() {

	}
	
	public static ListFragmentTab getInstance() {
	      
		if(instance == null) {
	         instance = new ListFragmentTab();
	    }
	    return instance;
	}	  
	
	
	
	@Override
	  public View onCreateView(LayoutInflater inflater, 
	                 ViewGroup container, Bundle savedInstanceState) {
		
		this.inflater = inflater;
		this.container = container;
		this.savedInstanceState = savedInstanceState;
		
		userList = new ArrayList<Connection>();
	    // Inflate the layout for this fragment
	    view = inflater.inflate(R.layout.list_users, container, false);

	    // Se vincula Adaptador
	    admin = ConnectionSQLite.getInstance(getActivity());
        //admin = new ConnectionSQLite(this.getActivity());
        SQLiteDatabase db = admin.getWritableDatabase();
	    adapter = new Adapter(this,admin.getAllUsers());
	    userList = admin.getAllUsers();
        setListAdapter(adapter);
	   
	    
	    return view;
	  }
	
	
	@Override
	public void onStart() {
	    super.onStart();  

	    admin = ConnectionSQLite.getInstance(getActivity());
    	adapter = new Adapter(this,admin.getAllUsers());
        adapter.setList(admin.getAllUsers());
        setListAdapter(adapter);   

	}
	    
	  
	    @Override
		public void onListItemClick(ListView listView, View view, int position, long id) {
	    	

			super.onListItemClick(listView, view, position, id);
			
			setO(getListAdapter().getItem(position));
			
			
			final CheckBox iconFav = (CheckBox) listView.findViewById(R.id.checkFav);
			if (((Connection) o).getFav().equalsIgnoreCase("true")){
		    	iconFav.setButtonDrawable(R.drawable.star_ful);

		    }
		    else{
		    	iconFav.setButtonDrawable(R.drawable.star_emp);
		    }			
			
			((ClientActivityTabs)getActivity()).setO(getO());			
			
			//SuperListener parentFragment;
			DialogOptions dialog1 = DialogOptions.newInstance((SuperListener) this);
			dialog1.show(getFragmentManager(),"dialog");
		
		}
	    
	    /*
		private void deleting(){
			//UsersSQLite admin = new UsersSQLite(this);
	        SQLiteDatabase db = admin.getWritableDatabase();
	        Toast.makeText(this.getSherlockActivity().getBaseContext(), "User deleted", Toast.LENGTH_SHORT).show();
	        admin.deleteUser((Connection) getO());
	        adapter.setList(admin.getAllUsers());
	        setListAdapter(adapter);	        	        
	        
		}*/

		@Override
		public void deleteUser() {
			
	        SQLiteDatabase db = admin.getWritableDatabase();
	        admin.deleteUser((Connection) getO());
	        adapter.setList(admin.getAllUsers());
	        setListAdapter(adapter);
			
		}
		

		@Override
		public void editingUser() {
			
			//ConnectionSQLite admin = new ConnectionSQLite(this.getActivity());
			admin = ConnectionSQLite.getInstance(getActivity());
			SQLiteDatabase db = admin.getWritableDatabase();
			
	        Intent intent = new Intent (this.getActivity(),EditionActivity.class);
	        
	        intent.putExtra("Name", ((Connection) getO()).getName());
	        intent.putExtra("IP", ((Connection) getO()).getIP());
	        intent.putExtra("PORT", ((Connection) getO()).getPORT());
	        intent.putExtra("PSW", ((Connection) getO()).getPsw());

	        startActivity(intent);
	        
		}
			

		/**
		 * 
		 * @param user
		 */
		public void refreshFavorites(Connection user) {
			admin.updateUser(user);
			
		}

		/**
		 * 
		 * @return
		 */
		public static Object getO() {
			return o;
		}

		public static void setO(Object o) {
			ListFragmentTab.o = o;
		}


	
	    
	  	  

}
