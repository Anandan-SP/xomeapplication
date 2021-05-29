package com.xomeapp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class SearchActivity : AppCompatActivity() {

    var myRecyclerView: RecyclerView? = null
    var myFlickerPojoList: ArrayList<FlickerPojo> = ArrayList()
    var mySearchET: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        setContentView(R.layout.activity_search)
        classAndWidgetInitialize()
    }

    /**
     * Initialize the views and widgets
     */
    private fun classAndWidgetInitialize() {
        myRecyclerView = findViewById(R.id.activity_Search_RCYLV)
        mySearchET = findViewById(R.id.activity_Search_ET_edittext)
        onKeyboardSearchListener()
    }

    /**
     * Keyboard search listener while clicks search
     */
    private fun onKeyboardSearchListener() {
        mySearchET!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getUsers(mySearchET!!.text.toString());
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    /**
     * Get the values depending upon the search strings
     */
    private fun getUsers(aSearch: String) {
        val queue = Volley.newRequestQueue(this)
        val url: String =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&" +
                    "format=json&nojsoncallback=1&safe_search=1&text=" + aSearch

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                var aResponseStr = response.toString()
                val aParentObject: JSONObject = JSONObject(aResponseStr)
                val aPhotoObject: JSONObject = aParentObject.getJSONObject("photos")
                val jsonArray: JSONArray = aPhotoObject.getJSONArray("photo")
                if (jsonArray.length() > 0) {
                    myFlickerPojoList.clear()
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        val aPojo = FlickerPojo()
                        aPojo.id = jsonInner.getString("id")
                        aPojo.owner = jsonInner.getString("owner")
                        aPojo.secret = jsonInner.getString("secret")
                        aPojo.server = jsonInner.getString("server")
                        aPojo.farm = jsonInner.getString("farm")
                        aPojo.title = jsonInner.getString("title")
                        aPojo.ispublic = jsonInner.getString("ispublic")
                        aPojo.isfriend = jsonInner.getString("isfriend")
                        aPojo.isfamily = jsonInner.getString("isfamily")
                        myFlickerPojoList.add(aPojo)
                    }
                }
                loadAdapter(myFlickerPojoList)
            },
            Response.ErrorListener { })
        queue.add(stringReq)
    }

    /**
     * Load the adapter
     */
    private fun loadAdapter(aFlickerPojoList: ArrayList<FlickerPojo>) {
        hideKeyboard()
        val aGridLayoutManager = GridLayoutManager(this, 3)
        myRecyclerView!!.layoutManager = aGridLayoutManager
        myRecyclerView!!.setHasFixedSize(true)
        val aAdapter = FlickerGridAdapter(
            this@SearchActivity,
            aFlickerPojoList
        )
        myRecyclerView!!.adapter = aAdapter
    }

    /**
     * Hide the keyboard after result shows
     */
    private fun hideKeyboard() {
        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}