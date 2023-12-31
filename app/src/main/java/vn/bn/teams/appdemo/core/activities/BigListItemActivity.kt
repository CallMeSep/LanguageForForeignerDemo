package vn.bn.teams.appdemo.core.activities


//import vn.bn.teams.appdemo.api.ApiInterface
//import vn.bn.teams.appdemo.api.RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import vn.bn.teams.appdemo.api.SessionManager
import vn.bn.teams.appdemo.core.adapter.BigListFollowAdapter
import vn.bn.teams.appdemo.core.utils.DialogUtil
import vn.bn.teams.appdemo.data.Constants
import vn.bn.teams.appdemo.data.database.MyDatabase
import vn.bn.teams.appdemo.data.models.DataFollow
import vn.bn.teams.appdemo.databinding.ActivityBigListFollowBinding


class BigListItemActivity : AppCompatActivity() {

    private var gridLayoutManager: GridLayoutManager? = null
    private var bigListAdapter: BigListFollowAdapter? = null
    var key: String? = null
    private lateinit var sessionManager: SessionManager
    lateinit var binding: ActivityBigListFollowBinding
    private var db: MyDatabase? = null
    private var list: ArrayList<DataFollow>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBigListFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        //getData()
        db = MyDatabase(this)

//        list = db?.getAllItemsBigList()
        list = db?.getAllTheme(this)

        initRecyclerview()
        initView()
        onClick()
    }


    private fun onClick() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this@BigListItemActivity, HomeScreenActivity::class.java)
            startActivity(intent)
        }

        binding.edtText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
        })

    }

    private fun filterData(query: String?) {
        list!!.clear()
        if (query.isNullOrBlank()) {
            list = db?.getAllTheme(context = this)
        } else {
            for (item in db?.getAllTheme(context = this)!!) {
                if (item.title.lowercase().contains(query.lowercase())) {
                    list!!.add(item)
                }
            }
        }
        list?.let { bigListAdapter!!.setData(it) }
    }


    private fun initView() {
        val title = intent.getStringExtra(Constants.KEY_HOME)
        key = intent.getStringExtra(Constants.KEY_HOME_FOLLOW)
        Log.d("__test", "onBindViewHolder: "+key)


        binding.txtTitle.text = title

    }

    private fun initRecyclerview() {
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        binding.listFollow.layoutManager = gridLayoutManager
        binding.listFollow.setHasFixedSize(true)
        bigListAdapter =BigListFollowAdapter(applicationContext, this)
        binding.listFollow.adapter = bigListAdapter
        bigListAdapter!!.setData(list!!)
        DialogUtil.progressDlgHide()
    }

}