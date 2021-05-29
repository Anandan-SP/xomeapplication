package com.xomeapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    var aSearchStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide()
        setContentView(R.layout.activity_search)
        classAndWidgetInitialize()
    }

    private fun classAndWidgetInitialize() {
        myRecyclerView = findViewById(R.id.activity_Search_RCYLV)
        mySearchET = findViewById(R.id.activity_Search_ET_edittext)
        textChangedListener()
    }

    private fun textChangedListener() {
        mySearchET!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length >= 3) {
                    getUsers(s.toString());
                    aSearchStr = s.toString();
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun getUsers(aSearch: String) {
        val queue = Volley.newRequestQueue(this)
        val url: String =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&" +
                    "format=json&nojsoncallback=1&safe_search=1&text=" + aSearch

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                Log.e("strResp", strResp)
                val aParentObject: JSONObject = JSONObject(strResp)
                val aPeopleObject: JSONObject = aParentObject.getJSONObject("photos")
                val jsonArray: JSONArray = aPeopleObject.getJSONArray("photo")
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

    private fun loadAdapter(aFlickerPojoList: ArrayList<FlickerPojo>) {

        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

        val gridLayoutManager = GridLayoutManager(this, 3)
        myRecyclerView!!.layoutManager = gridLayoutManager
        val aAdapter = FlickerGridAdapter(
            this@SearchActivity,
            aFlickerPojoList
        )
        myRecyclerView!!.adapter = aAdapter
    }

}