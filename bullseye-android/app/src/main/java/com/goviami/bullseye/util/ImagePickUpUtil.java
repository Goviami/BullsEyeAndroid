package com.goviami.bullseye.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goviami.bullseye.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by subbu on 11/9/2014.
 */
public class ImagePickUpUtil {
    private Activity context;
    private LayoutInflater inflater;
    private List<String> showGalOptions;

    public ImagePickUpUtil(Activity context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        showGalOptions = Arrays.asList(context.getResources().getStringArray(R.array.allowedGalOptions));
    }

    /**
     * Detect the available intent and open a new dialog.
     */
    public void openImageSelector() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent gallIntent=new Intent(Intent.ACTION_GET_CONTENT);
        gallIntent.setType("image/*");

        List<ResolveInfo> info=new ArrayList<ResolveInfo>();
        List<Intent> yourIntentsList = new ArrayList<Intent>();
        PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(camIntent, 0);
        for (ResolveInfo res : listCam) {
            final Intent finalIntent = new Intent(camIntent);
            finalIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            yourIntentsList.add(finalIntent);
            info.add(res);
        }

        List<ResolveInfo> listGall = packageManager.queryIntentActivities(gallIntent, 0);
        for (ResolveInfo res : listGall) {
            final Intent finalIntent = new Intent(gallIntent);
            if(showGalOptions.contains(res.loadLabel(context.getPackageManager()))) {
                finalIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                yourIntentsList.add(finalIntent);
                info.add(res);
            }
        }

        // show available intents
        openDialog(context,yourIntentsList,info);
    }

    /**
     * Open a new dialog with the detected items.
     *
     * @param context
     * @param intents
     * @param activitiesInfo
     */
    private void openDialog(final Activity context, final List<Intent> intents,
                                   List<ResolveInfo> activitiesInfo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCustomTitle(getTitleView(context));
        dialog.setAdapter(buildAdapter(context, activitiesInfo),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = intents.get(id);
                        context.startActivityForResult(intent, 1);

                    }
                });

        dialog.setNeutralButton(context.getResources().getString(R.string.cancel_btn),
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    /**
     * Build the list of items to show using the image_text_row layout.
     * @param context
     * @param activitiesInfo
     * @return
     */
    private ArrayAdapter<ResolveInfo> buildAdapter(final Context context,final List<ResolveInfo> activitiesInfo) {
        return new ArrayAdapter<ResolveInfo>(context, R.layout.image_text_row,activitiesInfo){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View cellView = convertView;
                Cell cell;
                ResolveInfo res = activitiesInfo.get(position);

                if (convertView == null) {
                    cell = new Cell();
                    cellView = inflater.inflate(R.layout.country_spinner_row, null);
                    cell.textView = (TextView) cellView.findViewById(R.id.row_title);
                    cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
                    cellView.setTag(cell);
                } else {
                    cell = (Cell) cellView.getTag();
                }

                cell.textView.setText(res.loadLabel(context.getPackageManager()));
                cell.imageView.setImageDrawable(res.loadIcon(context.getPackageManager()));
                return cellView;
            }
        };
    }

    private View getTitleView(final Activity context){
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.title_orange_view, null);
        //Set Title
        TextView dTitle = (TextView) view.findViewById(R.id.dialogTitle);
        dTitle.setText(R.string.profile_pic_dialog_tilte);
        return view;
    }

    /**
     * Holder for the cell
     *
     */
    static class Cell {
        public TextView textView;
        public ImageView imageView;
    }

}