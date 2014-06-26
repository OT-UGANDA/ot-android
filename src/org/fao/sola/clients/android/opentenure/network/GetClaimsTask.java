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
package org.fao.sola.clients.android.opentenure.network;


import org.fao.sola.clients.android.opentenure.OpenTenureApplication;
import org.fao.sola.clients.android.opentenure.R;
import org.fao.sola.clients.android.opentenure.network.response.Claim;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetClaimsTask extends AsyncTask<Claim, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Claim... params) {

		return GetClaims.execute(params);
	}

	@Override
	protected void onPostExecute(final Boolean result) {

		Toast toast;

		if (result) {

			toast = Toast.makeText(OpenTenureApplication.getContext(),
					OpenTenureApplication.getContext().getResources().getString(R.string.message_claims_downloaded), Toast.LENGTH_SHORT);
			toast.show();

		OpenTenureApplication.getMapFragment().refreshMap();
		OpenTenureApplication.getPersonsFragment().refresh();
			
			

		} else {

			toast = Toast.makeText(OpenTenureApplication.getContext(),
					OpenTenureApplication.getContext().getResources().getString(R.string.message_error_downloading_claims), Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
