package com.pinboard



import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {}

//
//    private var mAuth: FirebaseAuth? = null
//    private var mDatabase: DatabaseReference? = null
//    private var mMessageReference: DatabaseReference? = null
//
//
//    fun firebaseConnect() {
//        mDatabase = FirebaseDatabase.getInstance().reference
//        mMessageReference = FirebaseDatabase.getInstance().getReference("message")
//    }
//
//
//    fun onClickButton(view: View) {
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, Serg!")
//        val myToast = Toast.makeText(this, "DONE!", Toast.LENGTH_LONG)
//        myToast.show()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        mAuth = FirebaseAuth.getInstance();
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        //Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////         Handle action bar item clicks here. The action bar will
////         automatically handle clicks on the Home/Up button, so long
////         as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//}

