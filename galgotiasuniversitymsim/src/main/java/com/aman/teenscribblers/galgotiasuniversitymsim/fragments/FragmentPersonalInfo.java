package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AlertDialogManager;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
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

    ListView list;
    ProgressBar pb;
    Boolean isInternetPresent;
    Connection_detect cd;
    String mUsername, mPassword;
    FileInputStream input;
    DataInputStream din;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_personalinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppConstants.ids = new ArrayList<>();
        AppConstants.idvalues = new ArrayList<>();
        AppConstants.titletext = new ArrayList<>();
        cd = new Connection_detect(getActivity().getApplicationContext());
        pb = (ProgressBar) view.findViewById(R.id.progressBar_personal);
        idlist();
        titles();
        list = (ListView) view.findViewById(R.id.listView_personal);
        try {
            input = getActivity().openFileInput("user.txt"); // Open input
            // stream
            din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) { // Read lines
                String line = din.readUTF();
                AppConstants.idvalues.add(line);
            }
            din.close();
            pb.setVisibility(View.GONE);
            setadapter();

        } catch (FileNotFoundException e) {
            new Info().execute();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error Reading file!", Toast.LENGTH_SHORT)
                    .show();
        }

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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
            isInternetPresent = cd.isConnectingToInternet();
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

                } else if (doc != null)
                    AppConstants.idvalues.add(doc.text());
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
                AlertDialogManager.showAlertDialog(getActivity(),
                        "Error retriving data!");

            } else {
                setadapter();

            }
        }
    }

    public class CardsAdapter extends ArrayAdapter<String> {

        public TextView text, textvalue;

        public CardsAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
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
            //text.setTypeface(newsfont);
            text.setText(AppConstants.titletext.get(position));

            textvalue = (TextView) row.findViewById(R.id.value);
            //textvalue.setTypeface(newsfont);
            if (!AppConstants.idvalues.isEmpty())
                textvalue.setText(AppConstants.idvalues.get(position));
            else
                textvalue.setText("-");
            return row;
        }

    }

}
