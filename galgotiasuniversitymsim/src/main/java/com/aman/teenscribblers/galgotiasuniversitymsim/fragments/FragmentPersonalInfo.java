package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.Service.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentPersonalInfo extends Fragment {

    private static final String TAG = "PersonalInfo Fragment";
    private ListView list;
    private ProgressBar pb;
    private Connection_detect cd;
    private String mUsername, mPassword, admno, name, email;
    private View rootview;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_personalinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        AppConstants.ids = new ArrayList<>();
        AppConstants.idvalues = new ArrayList<>();
        AppConstants.titletext = new ArrayList<>();
        cd = new Connection_detect(getActivity().getApplicationContext());
        pb = (ProgressBar) view.findViewById(R.id.progressBar_personal);
        idlist();
        titles();
        mUsername = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "Username");
        mPassword = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "Password");
        list = (ListView) view.findViewById(R.id.listView_personal);
        try {
            FileInputStream input = getActivity().openFileInput("user.txt");
            // stream
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) { // Read lines
                String line = din.readUTF();
                AppConstants.idvalues.add(line);
                if (i == 0)
                    admno = line;
                else if (i == 2)
                    name = line;
                else if (i == 11)
                    email = line;
            }
            din.close();
            pb.setVisibility(View.GONE);
            AddGCMregtoPref();
            GCMCalls();
            setadapter();

        } catch (FileNotFoundException e) {
            new Info().execute();
        } catch (IOException e) {
            Snackbar.make(view, "Error Reading File", Snackbar.LENGTH_LONG).show();
        }

    }

    void AddGCMregtoPref() {
        PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_USER_ADMNO_KEY, admno);
        PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_USER_EMAIL_KEY, email);
        PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_USER_NAME_KEY, name);
    }

    void addtolist(String s) {
        AppConstants.ids.add(s);
    }

    void addtotitlelist(String s) {
        AppConstants.titletext.add(s);
    }

    private void setadapter() {
        CardsAdapter adapter = new CardsAdapter(getActivity(),
                android.R.layout.simple_list_item_1);
        ScaleInAnimationAdapter scaleAnimationAdapter = new ScaleInAnimationAdapter(
                adapter);
        scaleAnimationAdapter.setAbsListView(list);
        assert scaleAnimationAdapter.getViewAnimator() != null;
        scaleAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                300);
        list.setAdapter(scaleAnimationAdapter);
    }

    private void titles() {
        addtotitlelist("Admission No");
        addtotitlelist("Reg. Form No");
        addtotitlelist("Name");
        addtotitlelist("Gender");
        addtotitlelist("Blood Group");
        addtotitlelist("DOB");
        addtotitlelist("Present Address");
        addtotitlelist("City");
        addtotitlelist("State");
        addtotitlelist("Pin code");
        addtotitlelist("Phone");
        addtotitlelist("Email ID");
        addtotitlelist("Remark");
        addtotitlelist("Local Guardian Name");
        addtotitlelist("Local Guardian Address");
        addtotitlelist("Local Guardian Phone No");
        addtotitlelist("Father Name");
        addtotitlelist("Father's Mobile");
        addtotitlelist("Father's E-Mail");
        addtotitlelist("Mother Name");
        addtotitlelist("Mother's Mobile");
        addtotitlelist("Mother's E-Mail");
        addtotitlelist("Occupation");
        addtotitlelist("Designation");
        addtotitlelist("Monthly Income");
        addtotitlelist("Permanent Address");
        addtotitlelist("City");
        addtotitlelist("State");
        addtotitlelist("Pin code");
        addtotitlelist("Phone");
        addtotitlelist("Mobile No");
    }

    private void idlist() {
        addtolist("MCPH1_SCPH_lblAdmNo");
        addtolist("MCPH1_SCPH_lblRegNo");
        addtolist("MCPH1_SCPH_lblName");
        addtolist("MCPH1_SCPH_lblGender");
        addtolist("MCPH1_SCPH_lblBG");
        addtolist("MCPH1_SCPH_lblDOB");
        addtolist("MCPH1_SCPH_lblPresentAdd");
        addtolist("MCPH1_SCPH_lblCity1");
        addtolist("MCPH1_SCPH_lblstate1");
        addtolist("MCPH1_SCPH_lblPin1");
        addtolist("MCPH1_SCPH_lblPhone1");
        addtolist("MCPH1_SCPH_lblEmail1");
        addtolist("MCPH1_SCPH_lblRemark");
        addtolist("MCPH1_SCPH_lblLGuard");
        addtolist("MCPH1_SCPH_lblAddress1");
        addtolist("MCPH1_SCPH_lblPhone");
        addtolist("MCPH1_SCPH_lblfather");
        addtolist("MCPH1_SCPH_lblfmob");
        addtolist("MCPH1_SCPH_lblFEmail");
        addtolist("MCPH1_SCPH_lblmother");
        addtolist("MCPH1_SCPH_lblMobile");
        addtolist("MCPH1_SCPH_lblMEmail");
        addtolist("MCPH1_SCPH_lblOccupation");
        addtolist("MCPH1_SCPH_lblDesi");
        addtolist("MCPH1_SCPH_lblMon");
        addtolist("MCPH1_SCPH_lblParmanantAdd");
        addtolist("MCPH1_SCPH_lblCity2");
        addtolist("MCPH1_SCPH_lblState2");
        addtolist("MCPH1_SCPH_lblpin2");
        addtolist("MCPH1_SCPH_lblPhone2");
        addtolist("MCPH1_SCPH_lblmob");

    }

    public class Info extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            boolean a;
            String s = "error";
            Boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                try {
                    a = IonMethods.simlogin(mUsername, mPassword);
                    if (a)
                        s = IonMethods.getString(AppConstants.PersonalInfoString);
                } catch (Exception e) {
                    e.printStackTrace();
                    s = "error";
                }
            }

            if (s == null || s.equals("error")) {
                return "Error";
            }
            Document document = Jsoup.parse(s);
            int i = 0;
            while (i != AppConstants.ids.size()) {
                Element doc = document.getElementById(AppConstants.ids
                        .get(i));
                if (doc != null && doc.text().equals("")) {
                    AppConstants.idvalues.add("-");
                } else if (doc != null) {
                    AppConstants.idvalues.add(doc.text());
                    if (i == 0)
                        admno = doc.text();
                    else if (i == 2)
                        name = doc.text();
                    else if (i == 11)
                        email = doc.text();
                }
                // Log.d("values", doc.text());
                i++;
            }
            try {
                FileOutputStream output = getActivity().openFileOutput("user.txt",
                        Context.MODE_PRIVATE);
                DataOutputStream dout = new DataOutputStream(output);
                dout.writeInt(AppConstants.idvalues.size()); // Save line
                // count
                for (String line : AppConstants.idvalues)
                    // Save lines
                    dout.writeUTF(line);
                dout.flush(); // Flush stream ...
                dout.close(); // ... and close.
            } catch (FileNotFoundException e) {
                return "Error";
            } catch (IOException e) {
                return "Error";
            }
            return s;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            if (result.equals("Error")) {
                if (getView() != null)
                    Snackbar.make(getView(), "There is some error.Please Re-login for proper Use", Snackbar.LENGTH_INDEFINITE).show();

            } else {
                setadapter();
                AddGCMregtoPref();
                GCMCalls();
            }
        }
    }

    private void GCMCalls() {
        //GCM
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(AppConstants.SENT_TOKEN_TO_SERVER, false);
                if (!sentToken)
                    Snackbar.make(rootview, "Notification Registration failed.You will not recieve any messages.", Snackbar.LENGTH_LONG).show();
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
            getActivity().startService(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(AppConstants.REGISTRATION_COMPLETE));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void onPause() {
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Snackbar.make(rootview, "This device is not supported for Notifications.", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    public class CardsAdapter extends ArrayAdapter<String> {

        public TextView text, textvalue;

        public CardsAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return AppConstants.ids.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;

            if (row == null) {

                row = getActivity().getLayoutInflater().inflate(R.layout.list_msim, parent,
                        false);
            }
            text = (TextView) row.findViewById(R.id.title);
            text.setText(AppConstants.titletext.get(position));

            textvalue = (TextView) row.findViewById(R.id.value);
            if (!AppConstants.idvalues.isEmpty())
                textvalue.setText(AppConstants.idvalues.get(position));
            else
                textvalue.setText("-");
            return row;
        }

    }


}
