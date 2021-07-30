package fahimeh.eltejaei.covidapp

import android.content.Context
import android.view.ViewGroup
import fahimeh.eltejaei.covidapp.model.Country

import android.view.LayoutInflater
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ListAdapter(private var list: List<Country>)
    : RecyclerView.Adapter<ConuntryViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConuntryViewHolder  {
        val inflater = LayoutInflater.from(parent.context)
        return ConuntryViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ConuntryViewHolder, position: Int) {
        val itemData: Country = list[position]
        holder.bind(itemData)
    }

    override fun getItemCount(): Int = list.size
    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Country>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item.Slug.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            list = filterResults?.values as MutableList<Country>
            notifyDataSetChanged()
        }

    }
}

class ConuntryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_list, parent, false)) {
    private var txt_country_name: TextView? = null
    private var txt_mobtala: TextView? = null
    private var txt_foot: TextView? = null
    private var txt_behbood: TextView? = null
    private var context: Context = parent.context

    init {
        txt_country_name = itemView.findViewById(R.id.txt_country_name)
        txt_mobtala = itemView.findViewById(R.id.txt_mobtala)
        txt_foot = itemView.findViewById(R.id.txt_foot)
        txt_behbood = itemView.findViewById(R.id.txt_behbood)

    }

    fun bind(itemData: Country) {
        txt_country_name?.text = itemData.Slug
        txt_mobtala?.text = context.getString(R.string.txt_new_confirmed) +" "+ itemData.NewConfirmed
        txt_foot?.text = context.getString(R.string.txt_new_death) +" "+ itemData.NewDeaths
        txt_behbood?.text = context.getString(R.string.txt_new_recoverd) +" "+ itemData.NewRecovered
    }

}


