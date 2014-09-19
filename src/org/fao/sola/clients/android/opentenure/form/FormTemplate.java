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
package org.fao.sola.clients.android.opentenure.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FormTemplate {
	@Override
	public String toString() {
		return "Form ["
				+ "sections=" + Arrays.toString(sectionTemplates.toArray())
				+ ", name=" + name
				+ ", displayName=" + displayName
				+ "]";
	}

	private String name;
	private String displayName;
	private List<SectionTemplate> sectionTemplates;
	
	public FormTemplate(String name){
		this.name = name;
		this.sectionTemplates = new ArrayList<SectionTemplate>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<SectionTemplate> getSections() {
		return sectionTemplates;
	}

	public void setSections(List<SectionTemplate> pages) {
		this.sectionTemplates = pages;
	}

	public FormTemplate(){
		this.sectionTemplates = new ArrayList<SectionTemplate>();
	}
	
	public FormTemplate(FormTemplate formTemplate){
		this.name = formTemplate.getName();
		this.displayName = formTemplate.getDisplayName();
		this.sectionTemplates = new ArrayList<SectionTemplate>();
		for(SectionTemplate sectionTemplate:formTemplate.getSections()){
			this.sectionTemplates.add(new SectionTemplate(sectionTemplate));
		}
	}
	
	public FormTemplate(FormPayload formPayload){
		this.name = formPayload.getFormTemplateName();
		this.displayName = formPayload.getFormTemplateName();
		this.sectionTemplates = new ArrayList<SectionTemplate>();
		for(SectionPayload sectionPayload:formPayload.getSections()){
			this.sectionTemplates.add(new SectionTemplate(sectionPayload));
		}
	}
	
	public void addSection(SectionTemplate sectionTemplate){
		sectionTemplates.add(sectionTemplate);
	}
	
	public FieldConstraint getFailedConstraint(FormPayload payload) {
		Iterator<SectionPayload> iter = payload.getSections().iterator();
		for(SectionTemplate pageTemplate:sectionTemplates){
			SectionPayload pagePayload = iter.next();
			if(pageTemplate.getFailedConstraint(pagePayload) != null){
				return pageTemplate.getFailedConstraint(pagePayload);
			}
		}
		return null;
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		  try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		  return null;
	}
	
	public static FormTemplate fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		FormTemplate form;
		try {
			form = mapper.readValue(json, FormTemplate.class);
			return form;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
