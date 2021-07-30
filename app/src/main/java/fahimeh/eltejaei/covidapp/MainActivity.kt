package fahimeh.eltejaei.covidapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import fahimeh.eltejaei.covidapp.model.Country
import fahimeh.eltejaei.covidapp.model.ResponseSummary
import fahimeh.eltejaei.covidapp.remote.ApiClient
import fahimeh.eltejaei.covidapp.remote.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

val baseUrl: String = "https://api.covid19api.com"

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var et_search: TextInputEditText
    lateinit var adapter: ListAdapter
    var searchWord: String = ""
    var data: List<Country> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        getApiData()

    }

    private fun initView() {
        tabLayout = findViewById(R.id.tab_layout)
        et_search = findViewById(R.id.et_search)
        adapter = ListAdapter(data)
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setAdapter(data)
                searchWord = s.toString()
                if (s?.length!! != 0) {
                    adapter.filter.filter(s)
                } else {
                    hideKeyboard(currentFocus ?: View(this@MainActivity))
                }
            }
        })
        val tab_confirmed = layoutInflater.inflate(R.layout.item_tab, null)
        (tab_confirmed.findViewById<View>(R.id.txt_tab) as TextView).text = getString(R.string.txt_max_confirmed)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tab_confirmed), 0)

        val tab_death = layoutInflater.inflate(R.layout.item_tab, null)
        (tab_death.findViewById<View>(R.id.txt_tab) as TextView).text = getString(R.string.txt_max_death)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tab_death), 1)

        val tab_recovered = layoutInflater.inflate(R.layout.item_tab, null)
        (tab_recovered.findViewById<View>(R.id.txt_tab) as TextView).text = getString(R.string.txt_max_recoverd)
        tab_layout.addTab(tab_layout.newTab().setCustomView(tab_recovered), 2)

        tabLayout.getTabAt(2)?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_right_unselected))
        tabLayout.getTabAt(0)?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_left_unselected))
        tabLayout.getTabAt(1)?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_unselected))

        tabLayout.selectTab(tabLayout.getTabAt(0))
        tabLayout.getTabAt(0)?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_left))
        (tabLayout.getTabAt(0)?.customView?.findViewById<View>(R.id.txt_tab) as TextView).setTextColor(resources.getColor(R.color.white))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_right_unselected))
                } else if (tab?.position == 0) {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_left_unselected))
                } else {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_unselected))
                }
                (tab?.customView?.findViewById<View>(R.id.txt_tab) as TextView).setTextColor(resources.getColor(R.color.grey_60))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 2) {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_right))
                    data = data.sortedBy { it.NewRecovered }.asReversed()
                } else if (tab?.position == 0) {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab_left))
                    data = data.sortedBy { it.NewConfirmed }.asReversed()
                } else {
                    tab?.view?.setBackground(resources.getDrawable(R.drawable.bg_tab))
                    data = data.sortedBy { it.NewDeaths }.asReversed()
                }
                setAdapter(data)
                if (searchWord?.length!! != 0) {
                    adapter.filter.filter(searchWord)
                }
                (tab?.customView?.findViewById<View>(R.id.txt_tab) as TextView).setTextColor(resources.getColor(R.color.white))
                hideKeyboard(currentFocus ?: View(this@MainActivity))
            }

        })
    }

    fun getApiData(): List<Country> {

        val client: ApiService = ApiClient.getClient()
        client.getSummery().enqueue(object :
            retrofit2.Callback<ResponseSummary> {
            override fun onFailure(call: Call<ResponseSummary>?, t: Throwable?) {

            }
            override fun onResponse(
                call: Call<ResponseSummary>?,
                response: Response<ResponseSummary>?
            ) {
                data = response?.body()?.Countries!!
                if (tabLayout.selectedTabPosition == 2) {
                    data = data.sortedBy { it.NewRecovered }.asReversed()
                } else if (tabLayout.selectedTabPosition == 0) {
                    data = data.sortedBy { it.NewConfirmed }.asReversed()
                } else {
                    data = data.sortedBy { it.NewDeaths }.asReversed()
                }
                setAdapter(data)
            }

        })
        return data
    }

    private fun setAdapter(data: List<Country>) {
        recycle_search.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = ListAdapter(data)
        recycle_search.adapter = adapter;
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}