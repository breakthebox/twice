package ch.unifr.pai.ice.client.dragNdrop;

/*
 * Copyright 2013 Pascal Bruegger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/***************************************************************************************************
 *  Project ICE 
 *  -----------
 *  PAI research Group - Dept. of Informatics 
 *  University of Fribourg - Switzerland
 *  Author: Pascal Bruegger 
 * 
 *************************************************************************************************/

import java.util.Collection;
import java.util.Vector;

import ch.unifr.pai.ice.client.ICEMain;
import ch.unifr.pai.ice.client.RequireInitialisation;
import ch.unifr.pai.ice.client.rpc.EventingService;
import ch.unifr.pai.ice.client.rpc.EventingServiceAsync;
import ch.unifr.pai.ice.client.tracking.CursorXY;
import ch.unifr.pai.ice.client.utils.ICEDataLogger;
import ch.unifr.pai.ice.shared.ExperimentIdentifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DnD4users extends VerticalPanel implements ICEDataLogger, RequireInitialisation {

	String blob1 = GWT.getModuleBaseURL() + "circle_black.png";
	String blob2 = GWT.getModuleBaseURL() + "circle_red.png";
	String blob3 = GWT.getModuleBaseURL() + "circle_blue.png";
	String blob4 = GWT.getModuleBaseURL() + "circle_green.png";
	String blob5 = GWT.getModuleBaseURL() + "circle_magenta.png";
	String blob6 = GWT.getModuleBaseURL() + "circle_violet.png";
	String blob7 = GWT.getModuleBaseURL() + "circle_grey.png";
	String blob8 = GWT.getModuleBaseURL() + "circle.png";

	DnD1userGeneric user1Panel = new DnD1userGeneric("user1", 20, blob1, this);
	DnD1userGeneric user2Panel = new DnD1userGeneric("user2", 20, blob2, this);
	DnD1userGeneric user3Panel = new DnD1userGeneric("user3", 20, blob3, this);
	DnD1userGeneric user4Panel = new DnD1userGeneric("user4", 20, blob4, this);

	HorizontalPanel hPanel1 = new HorizontalPanel();
	HorizontalPanel hPanel2 = new HorizontalPanel();

	int nbExpFinished = 0;
	int nbUser = 4;
	Vector<String> userLogVector = new Vector<String>();
	String[] loggedData;

	public DnD4users() {
		super();
		this.setSize("100%", "100%");
		hPanel1.setSize("100%", "100%");
		hPanel2.setSize("100%", "100%");

		user1Panel.setSize("100%", "100%");
		user2Panel.setSize("100%", "100%");
		user3Panel.setSize("100%", "100%");
		user4Panel.setSize("100%", "100%");

		hPanel1.add(user1Panel);
		// forces to maintain the size of the cell
		hPanel1.setCellWidth(user1Panel, "50%");
		hPanel1.setCellHeight(user1Panel, "50%");
		hPanel1.add(user2Panel);

		hPanel2.add(user3Panel);
		hPanel2.setCellWidth(user3Panel, "50%");
		hPanel2.setCellHeight(user3Panel, "50%");
		hPanel2.add(user4Panel);

		this.add(hPanel1);
		this.add(hPanel2);
		this.setBorderWidth(1);
	}

	@Override
	public void initialise() {
		user1Panel.initialise();
		user2Panel.initialise();
		user3Panel.initialise();
		user4Panel.initialise();
	}

	/**
	 * log the data and indicate if logged is OK or not
	 */
	private void log() {
		EventingServiceAsync svc = GWT.create(EventingService.class);
		svc.log(ICEMain.identifier, loggedData, ExperimentIdentifier.DRAGNDROP, 4, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				Window.alert("Successfully logged! Experiment finished");
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error:", caught);
				Window.alert("Not logged");
			}
		});
	}

	/**
	 * Format the logged data: comma separated value!
	 * 
	 * @param data
	 * @return Array of string containing the user, x, y coord, time stamps of the click
	 */

	private String[] getLoggedResult(String[][] data) {
		String[] result = new String[data.length];

		for (int i = 1; i < result.length; i++) {
			result[i] = data[i][0] + ";" + data[i][1] + ";" + data[i][2] + ";" + data[i][3] + " \n";
		}
		return result;
	}

	@Override
	public void setLoggedData(Collection<? extends String> blobData) {
		if (nbExpFinished < nbUser) {
			userLogVector.addAll(blobData);
			nbExpFinished++;
		}

		if (nbExpFinished == nbUser) {
			loggedData = new String[userLogVector.size()];
			userLogVector.copyInto(loggedData);
			log();
		}

	}

	/**
	 * Callback method used by the Blob class to send the vector of data to be logged.
	 * 
	 * @param blobData
	 */
	@Override
	public void setLoggedData(Vector<CursorXY> blobData) {

	}

	@Override
	public void setLoggedData(String[] blobData) {
		// TODO Auto-generated method stub

	}

}