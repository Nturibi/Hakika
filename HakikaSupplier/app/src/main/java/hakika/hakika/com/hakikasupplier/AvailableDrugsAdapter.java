//package hakika.hakika.com.hakikasupplier;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.Adapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class AvailableDrugsAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
//    @NonNull
//    @Override
//
//    private List<ExampleItem> exampleList;
//    private List<ExampleItem> exampleListFull;
//
//
//
//    class AvailableDrugswHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView textView1;
//
//        AvailableDrugswHolder(View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.image_view);
//            textView1 = itemView.findViewById(R.id.text_view1);
//        }
//    }
//
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_item, parent, false);
//        return new AvailableDrugswHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
//        ((AvailableDrugsHolder) holder).bindData(models.get(position));
//    }
//
//    @Override
//    public Filter getFilter() {
//        return AvailableDrugsFilter;
//    }
//
//    private Filter AvailableDrugsFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            return null;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    }
//}
