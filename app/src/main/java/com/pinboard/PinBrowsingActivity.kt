package com.pinboard


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.pinboard.Adapter.CardViewAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_pin_browsing.*


class PinBrowsingActivity : AppCompatActivity() {
	internal val TAG = "PinBrowsingActivity"

	var priceButtonClicked = false

	val sortingAdapter = GroupAdapter<ViewHolder>()

	private var user: FirebaseUser? = null

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null

	private var mAdapter: FirebaseRecyclerAdapter<Pin, PinViewHolder>? = null

	private var pinList: MutableList<Pin>? = mutableListOf()
	var recyclerView: RecyclerView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_browsing)

		setSupportActionBar(findViewById(R.id.toolbar))

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference =
			FirebaseDatabase.getInstance().getReference(getString(R.string.pin_folder_name))
		user = FirebaseAuth.getInstance().currentUser

		firebaseListenerInit()

		recyclerView = findViewById(R.id.pin_browsing_recycleviewer)

		recyclerView?.layoutManager = LinearLayoutManager(this)
		pin_browsing_recycleviewer.layoutManager = LinearLayoutManager(this)

		recyclerView?.adapter = sortingAdapter

		sortingAdapter.setOnItemClickListener { item, view ->
			val clickedPin = item as CardViewAdapter
			if (clickedPin.pin.userData?.userID.equals(FirebaseAuth.getInstance().uid)) {
				val intent = Intent(view.context, FullMyPinActivity::class.java)
				startActivity(intent.putExtra("pin", clickedPin.pin))
			} else {
				val intent = Intent(view.context, FullMyPinActivity::class.java)
				startActivity(intent.putExtra("pin", clickedPin.pin))
			}
		}

	}

	override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

		R.id.add_new_pin -> {
			val intent = Intent(this, CreatePinActivity::class.java)
			startActivity(intent)
			true
		}

//		R.id.search -> {
////			val intent = Intent(this, CreatePinActivity::class.java)
////			startActivity(intent)
//			sortingAdapter.clear()
//			pinList?.sortBy { pin -> pin.price?.toBigDecimal() }
//			pinList?.forEach { pin -> sortingAdapter.add(CardViewAdapter(pin)) }
//			recyclerView?.adapter = sortingAdapter
//			true
//		}
		R.id.sort_by_author_menuItem -> {
//			recyclerView?.scrollToPosition(5)
//			Toast.makeText(
//					this@PinBrowsingActivity,
//					"WRONG",
//					Toast.LENGTH_SHORT
//				).show()
			true
		}

		R.id.sort_by_price_menuItem -> {
			sortingAdapter.clear()
			if (!priceButtonClicked) {
				pinList?.sortBy { pin -> pin.price?.toBigDecimal() }
			} else pinList?.sortByDescending { pin -> pin.price?.toBigDecimal() }
			pinList?.forEach { pin -> sortingAdapter.add(CardViewAdapter(pin)) }
			recyclerView?.adapter = sortingAdapter
			priceButtonClicked = !priceButtonClicked
			true
		}

		R.id.show_my_pins_menuItem -> {
			sortingAdapter.clear()

			if (item.title.equals("Show only my PINs")) {
				item.title = "Show ALL PINs"
				for (i in 0 until pinList!!.size) {
					if (pinList!!.elementAt(i).userData?.userID.equals(user?.uid)) {
						sortingAdapter.add(CardViewAdapter(pinList!!.elementAt(i)))
					}
				}
			} else if (item.title.equals("Show ALL PINs")) {
				item.title = "Show only my PINs"
				pinList?.forEach { pin -> sortingAdapter.add(CardViewAdapter(pin)) }
			}
			recyclerView?.adapter = sortingAdapter
			true
		}

		R.id.logout_menu_button -> {
			val intent = Intent(this, LoginActivity::class.java)
			intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			true
		}

		else -> {
			super.onOptionsItemSelected(item)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu)
			: Boolean {
		menuInflater.inflate(R.menu.menu_add_pin_search, menu)
		val searchItem = menu.findItem(R.id.search_toolbar)
		val searchView = searchItem.actionView as SearchView
		searchView.queryHint = "Search..."

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

			override fun onQueryTextChange(newText: String): Boolean {
				sortingAdapter.clear()
				for (i in 0 until pinList!!.size) {
					if (pinList!![i].contain(newText.toLowerCase())!!) sortingAdapter.add(
						CardViewAdapter(pinList!![i])
					)
				}
				return false
			}

			override fun onQueryTextSubmit(query: String): Boolean {
				sortingAdapter.clear()
				for (i in 0 until pinList!!.size) {
					if (pinList!![i].contain(query.toLowerCase())!!) sortingAdapter.add(
						CardViewAdapter(pinList!![i])
					)
				}
				return false
			}
		})
		return true
	}

	override fun onNavigateUpFromChild(child: Activity?): Boolean {
		sortingAdapter.clear()
		pinList?.forEach { pin -> sortingAdapter.add(CardViewAdapter(pin)) }
		recyclerView?.smoothScrollToPosition(sortingAdapter.itemCount)
		return super.onNavigateUpFromChild(child)
	}

	private fun firebaseListenerInit() {

		//val childEventListener = object : ChildEventListener {
		mMessageReference?.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)
				pinList?.add(message!!)
				sortingAdapter.add(CardViewAdapter(message!!))
//				recyclerView?.layoutManager = LinearLayoutManager(this@PinBrowsingActivity)
//				recyclerView?.smoothScrollToPosition(sortingAdapter.itemCount)

			}

			override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)

			}

			override fun onChildRemoved(dataSnapshot: DataSnapshot) {
				val message = dataSnapshot.getValue(Pin::class.java)
				//sortingAdapter.add(CardViewAdapter(message!!))
				pinList?.remove(message)
				sortingAdapter.clear()
				pinList?.forEach { pin -> sortingAdapter.add(CardViewAdapter(pin)) }
			}

			override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)

			}

			override fun onCancelled(databaseError: DatabaseError) {
				Log.e(TAG, "postMessages:onCancelled", databaseError.toException())
				Toast.makeText(
						this@PinBrowsingActivity,
						"Failed to load your Account. \n Please log in or register",
						Toast.LENGTH_SHORT
					)
					.show()
				finish()
				loginScreen()
			}

		})

	}

	internal fun loginScreen() {
		FirebaseAuth.getInstance().signOut()
		val intent = Intent(this, LoginActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
		finish()
		startActivity(intent)
	}

	override fun onDestroy() {
		super.onDestroy()

		sortingAdapter.clear()
	}

}

