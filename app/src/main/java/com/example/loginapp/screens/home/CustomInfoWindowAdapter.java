package com.example.loginapp.screens.home;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.loginapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import static com.example.loginapp.R.layout.custom_info_window;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View myMarkerView;
    public CustomInfoWindowAdapter(Context context) {
        this.myMarkerView = View.inflate(context, custom_info_window,null);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        TextView tvTitle = myMarkerView.findViewById(R.id.title);
        tvTitle.setText(marker.getTitle());
        android.widget.TextView tvSnippet = myMarkerView.findViewById(R.id.snippet);
        tvSnippet.setText(marker.getSnippet());
        return myMarkerView;
    }
}
