package com.kad.carbrokefinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.security.Provider;
import java.util.List;

public class ServicesAdapter extends  RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>{

    List<Service> servicesList;
Context context;
    public SelectedItem selectedItem;
    public ServicesAdapter(List<Service> servicesList,SelectedItem mSelectedItem, Context context) {
        this.servicesList = servicesList;
        this.context = context;
        this.selectedItem = mSelectedItem;
    }

    @Override
    public ServicesAdapter.ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServicesAdapter.ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {

        //ParseFile image = (ParseFile) parseList.get(position).get("logo");
        String url = servicesList.get(position).getImageUrl();
             //   image.getUrl();
        Glide.with(context)
                .load(url)
                //.placeholder(R.drawable.piwo_48)
               // .transform(new CircleTransform(context))
                .circleCrop()
                .into(holder.serviceBtn);

     //   holder.serviceBtn.setDrsetText(customerList.get(position).getName;
//        holder.customeraddress.setText(customerList.get(position).getAddress;
//        holder.customerphone.setText(customerList.get(position).getPhone;

    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageButton serviceBtn;
        //public TextView customername, customeraddress, customerphone;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceBtn = itemView.findViewById(R.id.serviceImage);
            serviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem.selectedItem(servicesList.get(getAdapterPosition()));
                }
            });
//            customeraddress = view.findViewById(R.id.txtCustomerAddress);
//            customerphone = view.findViewById(R.id.txtCustomerPhone);
        }
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public interface SelectedItem{
        void selectedItem(Service service);
    }
}