package sintad.org.sintad_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import sintad.org.sintad_mobile.R;
import sintad.org.sintad_mobile.model.ServiceOrder;

/**
 * Created by TTR on 21/09/2017.
 */

public class ServiceOrderAdapter extends ArrayAdapter<ServiceOrder> {
    private Activity context;
    private int resource;
    private List<ServiceOrder> order;

    public ServiceOrderAdapter(Activity mContext, int resource, List<ServiceOrder> order) {
        super(mContext, resource, order);
        this.context = mContext;
        this.resource = resource;
        this.order = order;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ServiceOrder item = order.get(position);
        LinearLayout rowView;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String date_order = formatter.format(item.getFechaOrden());
        if(convertView == null) {
            rowView = new LinearLayout(getContext());
            inflater.inflate(resource, rowView, true);
        } else {
            rowView = (LinearLayout) convertView;
        }

        TextView texto1 = (TextView) rowView.findViewById(R.id.txt1RowLay1);
        TextView texto2 = (TextView) rowView.findViewById(R.id.txt2RowLay1);
        TextView texto3 = (TextView) rowView.findViewById(R.id.txt3RowLay1);
        TextView texto4 = (TextView) rowView.findViewById(R.id.txt4RowLay1);
        TextView texto5 = (TextView) rowView.findViewById(R.id.txt5RowLay1);

        texto1.setText(item.getIdOrden());
        texto2.setText(date_order);
        texto3.setText(item.getRuc());
        texto4.setText(item.getNombreCliente());
        texto5.setText(item.getTipoServicio());
        return rowView;
    }
}
