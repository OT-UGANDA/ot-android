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
package org.fao.sola.clients.android.opentenure.form.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fao.sola.clients.android.opentenure.ModeDispatcher.Mode;
import org.fao.sola.clients.android.opentenure.R;
import org.fao.sola.clients.android.opentenure.form.FieldConstraint;
import org.fao.sola.clients.android.opentenure.form.FieldConstraintOption;
import org.fao.sola.clients.android.opentenure.form.FieldPayload;
import org.fao.sola.clients.android.opentenure.form.FieldTemplate;
import org.fao.sola.clients.android.opentenure.form.SectionElementPayload;
import org.fao.sola.clients.android.opentenure.form.SectionPayload;
import org.fao.sola.clients.android.opentenure.form.SectionTemplate;
import org.fao.sola.clients.android.opentenure.form.constraint.OptionConstraint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

public class SectionFragment extends ListFragment {

	private static final String SECTION_PAYLOAD_KEY = "sectionPayload";
	private static final String SECTION_TEMPLATE_KEY = "sectionTemplate";
	private View rootView;
	private SectionPayload sectionPayload;
	private SectionTemplate sectionTemplate;
	private Mode mode;

	public SectionFragment(SectionPayload section, SectionTemplate sectionTemplate, Mode mode){
		this.sectionTemplate = sectionTemplate;
		this.sectionPayload = section;
		this.mode = mode;
	}

	public SectionFragment(){
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.multiple_field_group, menu);
		if(mode == Mode.MODE_RO){
			MenuItem item = menu.findItem(R.id.action_new);
			item.setVisible(false);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			Intent intent = new Intent(rootView.getContext(),
					SectionElementActivity.class);
			intent.putExtra(SectionElementActivity.SECTION_ELEMENT_POSITION_KEY, SectionElementActivity.SECTION_ELEMENT_POSITION_NEW);
			intent.putExtra(SectionElementActivity.SECTION_ELEMENT_PAYLOAD_KEY, new SectionElementPayload(sectionTemplate).toJson());
			intent.putExtra(SectionElementActivity.SECTION_TEMPLATE_KEY, sectionTemplate.toJson());
			intent.putExtra(SectionElementActivity.MODE_KEY, mode.toString());
			startActivityForResult(intent, SectionElementActivity.SECTION_ELEMENT_ACTIVITY_REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data != null) { // No selection has been done

			if(resultCode == Activity.RESULT_OK
					&& requestCode == SectionElementActivity.SECTION_ELEMENT_ACTIVITY_REQUEST_CODE){
				
				String fieldGroup = data
				.getStringExtra(SectionElementActivity.SECTION_ELEMENT_PAYLOAD_KEY);
				int position = data
				.getIntExtra(SectionElementActivity.SECTION_ELEMENT_POSITION_KEY, SectionElementActivity.SECTION_ELEMENT_POSITION_NEW);

				if(position == SectionElementActivity.SECTION_ELEMENT_POSITION_NEW){
					// A new element was created and confirmed
					SectionElementPayload newSectionElement = SectionElementPayload.fromJson(fieldGroup);
					newSectionElement.setSectionPayloadId(sectionPayload.getId());
					sectionPayload.getSectionElementPayloadList().add(newSectionElement);
				}else{
					// Changes to an existing element have been made and confirmed
					SectionElementPayload newSectionElement = SectionElementPayload.fromJson(fieldGroup);
					sectionPayload.getSectionElementPayloadList().set(position, newSectionElement);
				}
				update();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.multiple_field_group_list, container,
				false);
		setHasOptionsMenu(true);

		if(savedInstanceState != null && savedInstanceState.getString(SECTION_PAYLOAD_KEY) != null){
			sectionPayload = SectionPayload.fromJson(savedInstanceState.getString(SECTION_PAYLOAD_KEY));
		}
		if(savedInstanceState != null && savedInstanceState.getString(SECTION_TEMPLATE_KEY) != null){
			sectionTemplate = SectionTemplate.fromJson(savedInstanceState.getString(SECTION_TEMPLATE_KEY));
		}
		update();
		InputMethodManager imm = (InputMethodManager) rootView.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInputFromWindow(rootView.getWindowToken(), 0, InputMethodManager.HIDE_IMPLICIT_ONLY);
		return rootView;
	}
	
	private void update() {

			List<SectionElementListTO> ownersListTOs = new ArrayList<SectionElementListTO>();

			for(SectionElementPayload sectionElement : sectionPayload.getSectionElementPayloadList()){
				
				SectionElementListTO fglto = new SectionElementListTO();
				fglto.setName(ownersListTOs.size() + "");
				StringBuffer sb = new StringBuffer();
				Iterator<FieldTemplate> iterator = sectionTemplate.getFieldTemplateList().iterator();
				for(FieldPayload field:sectionElement.getFieldPayloadList()){
					FieldTemplate template = iterator.next();
					if(sb.length() != 0){
						sb.append(",");
					}
					List<FieldConstraintOption> options = null;
					for(FieldConstraint constraint:template.getFieldConstraintList()){
						if(constraint instanceof OptionConstraint){
							options = ((OptionConstraint)constraint).getFieldConstraintOptionList(); 
						}
					}
					
					String fieldPayload = null;

					if(field.getStringPayload() != null)
						fieldPayload = field.getStringPayload();
					if(field.getBigDecimalPayload() != null)
						fieldPayload = field.getBigDecimalPayload().toString();
					if(field.getBooleanPayload() != null)
						fieldPayload = field.getBooleanPayload().toString();
					
					if(options != null){
						for(FieldConstraintOption option:options){
							if(fieldPayload.equalsIgnoreCase(option.getName())){
								fieldPayload = option.getDisplayName();
							}
						}
					}

					sb.append(fieldPayload);
				}
				fglto.setSlogan(sb.toString());
				fglto.setJson(sectionElement.toJson());
				ownersListTOs.add(fglto);
			}

			ArrayAdapter<SectionElementListTO> adapter = null;

			adapter = new SectionElementListAdapter(this, rootView.getContext(), ownersListTOs, sectionPayload, sectionTemplate, mode);

			setListAdapter(adapter);
			adapter.notifyDataSetChanged();

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(SECTION_PAYLOAD_KEY, sectionPayload.toJson());
		outState.putString(SECTION_TEMPLATE_KEY, sectionTemplate.toJson());
		super.onSaveInstanceState(outState);
	}
}
