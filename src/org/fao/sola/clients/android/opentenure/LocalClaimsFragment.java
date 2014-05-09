/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.fao.sola.clients.android.opentenure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import org.fao.sola.clients.android.opentenure.model.Claim;
import org.fao.sola.clients.android.opentenure.model.ClaimStatus;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LocalClaimsFragment extends ListFragment {

	private View rootView;
	private static final int CLAIM_RESULT = 100;
	private String mode;
	
	public void setMode(String mode){
		this.mode = mode;
	}

	public LocalClaimsFragment() {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.local_claims, menu);

		if(mode.equalsIgnoreCase(PersonActivity.MODE_RO)){
			menu.removeItem(R.id.action_new);
		}else{
			
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_new:
			Intent intent = new Intent(rootView.getContext(),
					ClaimActivity.class);
			intent.putExtra(ClaimActivity.CLAIM_ID_KEY, ClaimActivity.CREATE_CLAIM_ID);
			intent.putExtra(ClaimActivity.MODE_KEY, mode);
			startActivityForResult(intent, CLAIM_RESULT);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		default:
			update();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.local_claims_list, container,
				false);
		setHasOptionsMenu(true);
	    EditText inputSearch = (EditText) rootView.findViewById(R.id.filter_input_field);
	    inputSearch.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	    inputSearch.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	        }

	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

	        @Override
	        public void afterTextChanged(Editable arg0) {
	            ((LocalClaimsListAdapter)getListAdapter()).getFilter().filter(arg0.toString());
	        }
	    });

		update();

		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mode.equalsIgnoreCase(ClaimActivity.MODE_RW)) {
			Intent intent = new Intent(rootView.getContext(),
					ClaimActivity.class);
			intent.putExtra(ClaimActivity.CLAIM_ID_KEY, ((TextView)v.findViewById(R.id.claim_id)).getText());
			intent.putExtra(ClaimActivity.MODE_KEY, mode);
			startActivityForResult(intent, CLAIM_RESULT);
		}else{
			Intent resultIntent = new Intent();
			resultIntent.putExtra(ClaimActivity.CLAIM_ID_KEY, ((TextView)v.findViewById(R.id.claim_id)).getText());
			getActivity().setResult(SelectClaimActivity.SELECT_CLAIM_ACTIVITY_RESULT, resultIntent);
			getActivity().finish();
		}
	}

	protected void update() {
		List<Claim> claims = Claim.getAllClaims();
		List<ClaimListTO> claimListTOs = new ArrayList<ClaimListTO>();

		for(Claim claim : claims){
			ClaimListTO cto = new ClaimListTO();
			cto.setSlogan(claim.getName() + ", " + getResources().getString(R.string.by) + ": " + claim.getPerson().getFirstName()+ " " + claim.getPerson().getLastName());
			cto.setId(claim.getClaimId());
			if(claim.getStatus().equals(ClaimStatus._UPLOADING)) 
				cto.setStatus(claim.getStatus());
			else if(claim.getStatus().equals(ClaimStatus._UNMODERATED)){
				cto.setStatus("uploaded");
				}
			else cto.setStatus(" ");
			claimListTOs.add(cto);
		}
		ArrayAdapter<ClaimListTO> adapter = new LocalClaimsListAdapter(rootView.getContext(), claimListTOs);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();

	}
}
