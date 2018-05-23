package com.kftsoftwares.boutique.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

/**
 * Created by apple on 23/02/18.
 */

public class Util {


    public void showSingleOkAlert(final Context context, String message, String title)
    {
        if(!AppController.getInstance().mShowDialog)
        {

            AppController.getInstance().mShowDialog= true;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        AppController.getInstance().mShowDialog= false;
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
        }
        else {

        }
    }

    public void showAlert(Context context, String message, String title)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static String[] COUNTRY = new String[]{"Select Country","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",

            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",

            "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",

            "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",

            "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria",

            "Burkina Faso", "Burma (Myanmar)", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",

            "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",

            "Cocos (Keeling) Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica",

            "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",

            "Denmark", "Djibouti", "Dominica", "Dominican Republic",

            "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",

            "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia",

            "Gabon", "Gambia", "Gaza Strip", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",

            "Greenland", "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",

            "Haiti", "Holy See (Vatican City)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",

            "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica",

            "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait",

            "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",

            "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia",

            "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mayotte", "Mexico",

            "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco",

            "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",

            "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea",

            "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama",

            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",

            "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania", "Russia", "Rwanda",

            "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin",

            "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino",

            "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone",

            "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",

            "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland",

            "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",

            "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands",

            "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",

            "Wallis and Futuna", "West Bank", "Yemen", "Zambia", "Zimbabwe"};



    public static String[]  code = new String[]{"+93", "+355", "+213", "+1 684", "+376", "+244", "+1 264", "+672", "+1 268", "+54", "+374",

            "+297", "+61", "+43", "+994", "+1 242", "+973", "+880", "+1 246", "+375", "+32", "+501",

            "+229", "+1 441", "+975", "+591", "+387", "+267", "+55", "+246", "+1 284", "+673", "+359",

            "+226", "+95", "+257", "+855", "+237", "+1", "+238", "+1 345", "+236", "+235", "+56", "+86",

            "+61", "+891", "+57", "+269", "+682", "+506", "+385", "+53", "+357", "+420", "+243", "+45",

            "+253", "+1 767", "+1 849", "+1 829", "+1 809", "+593", "+20", "+503", "+240", "+291", "+372",

            "+251", "+500", "+298", "+679", "+358", "+33", "+689", "+241", "+220", "+970", "+995", "+49",

            "+233", "+350", "+30", "+299", "+1 473", "+1 671", "+502", "+224", "+245", "+592", "+509",

            "+379", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972",

            "+39", "+225", "+1 876", "+81", "+44", "+962", "+7", "+254", "+686", "+381", "+965", "+996",

            "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389",

            "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+222", "+230", "+262", "+52", "+691",

            "+373", "+377", "+976", "+382", "+1 664", "+212", "+258", "+264", "+674", "+977", "+31",

            "+599", "+687", "+64", "+505", "+227", "+234", "+683", "+672", "+850", "+1 670", "+47",

            "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+870", "+48", "+351", "+1",

            "+974", "+242", "+40", "+7", "+250", "+590", "+290", "+1 869", "+1 758", "+1 599", "+508",

            "+1 784", "+685", "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+421",

            "+386", "+677", "+252", "+27", "+82", "+34", "+94", "+249", "+597", "+268", "+46", "+41",

            "+963", "+886", "+992", "+255", "+66", "+670", "+228", "+690", "+676", "+1 868", "+216",

            "+90", "+993", "+1 649", "+688", "+256", "+380", "+971", "+44", "+1", "+598", "+1 340",

            "+998", "+678", "+58", "+84", "+681", "+970", "+967", "+260", "+263"};


    public void strikeColor(TextView textView,Context context)
    {
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context,R.color.black));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrikeThruText(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        float width = textView.getWidth();
        float heigh = textView.getHeight();
        canvas.drawLine(width/10, heigh/10, (width-width/10),(heigh-heigh/10), paint);

        textView.setPaintFlags(paint.getFlags());
    }

    public void checkConnection(Context context , boolean isConnected ) {

        if (isConnected)
        {
            showSingleOkAlert(context,"Internet Connected","Internet Status");
        }
        else {
            showSingleOkAlert(context,"No Internet Connection","Internet Status");
        }
    }

    public void showDialog(Context context , String message) {


            showSingleOkAlert(context,"Alert",message);

    }

    private static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }

    public  int getWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return width;
    }



}
