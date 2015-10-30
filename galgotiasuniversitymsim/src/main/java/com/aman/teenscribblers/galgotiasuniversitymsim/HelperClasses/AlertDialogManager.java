package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.Context;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.aman.teenscribblers.galgotiasuniversitymsim.R;
//import com.aman.teenscribblers.galgotiasuniversitymsim.view.MaterialReboundAlert;
//import com.squareup.picasso.Picasso;

public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param content - alert dialog title
     */
    public static void showAlertDialog(final Context context, String content) {
//        new MaterialDialog.Builder(context)
//                .title("Error")
//                .content(content)
//                .neutralText("ok")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onNeutral(MaterialDialog dialog) {
//                        super.onNeutral(dialog);
//                        dialog.cancel();
//                    }
//                })
//                .show();
    }

    public static void showAlertDialog(final Context context, String content, String title) {
//        new MaterialDialog.Builder(context)
//                .title(title)
//                .titleGravity(GravityEnum.CENTER)
//                .content(content)
//                .neutralText("ok")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onNeutral(MaterialDialog dialog) {
//                        super.onNeutral(dialog);
//                        dialog.cancel();
//                    }
//                })
//                .show();
    }

    public static void showAlertDialog(final Context context, String content, String title, String url) {
//        MaterialDialog.Builder dialogbuilder = new MaterialDialog.Builder(context)
//                .title(title)
//                .titleGravity(GravityEnum.CENTER)
//                .customView(R.layout.news_alert_layout, true)
//                .neutralText("ok")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onNeutral(MaterialDialog dialog) {
//                        super.onNeutral(dialog);
//                        dialog.cancel();
//                    }
//                });
//        MaterialDialog dialog = dialogbuilder.build();
//        View v = dialog.getCustomView();
//        TextView note = (TextView) v.findViewById(R.id.news_item_note);
//        ImageView image = (ImageView) v.findViewById(R.id.news_item_image);
//        note.setText(content);
//        Picasso picasso = Picasso.with(context);
//        picasso.setIndicatorsEnabled(false);
//        picasso.load(url).placeholder(R.drawable.logo)
//                .priority(Picasso.Priority.NORMAL)
//                .into(image);
//        dialog.show();
    }

    public static void showGQuasarAlert(Context context) {
//        MaterialReboundAlert.Builder dialogbuilder = new MaterialReboundAlert.Builder(context);
//        dialogbuilder.title("")
//                .customView(R.layout.gquasar_alert, true)
//                .positiveText("I\'m Waiting")
//                .autoDismiss(false)
//                .cancelable(false)
//                .positiveColor(R.color.ts_black)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        super.onPositive(dialog);
//                        dialog.cancel();
//
//                    }
//                });
//        MaterialReboundAlert dialog = MaterialReboundAlert.build(dialogbuilder);
//        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        dialog.show();
    }

    public static void showAboutDialog(Context context) {
//        MaterialReboundAlert.Builder dialogbuilder = new MaterialReboundAlert.Builder(context);
//        dialogbuilder
//                .customView(R.layout.activity_about_me, true)
//                .positiveText("Great Man!")
//                .positiveColor(R.color.ts_black)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        super.onPositive(dialog);
//                        dialog.cancel();
//
//                    }
//                });
//        MaterialReboundAlert dialog = MaterialReboundAlert.build(dialogbuilder);
//        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
//        dialog.show();
    }

}
