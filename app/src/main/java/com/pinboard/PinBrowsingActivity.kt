package com.pinboard


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.pinboard.Adapter.RecyclerItemAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class PinBrowsingActivity : AppCompatActivity() {
	internal val TAG = "PinBrowsingActivity"

	var priceButtonClicked = false

	val sortingAdapter = GroupAdapter<ViewHolder>()

	private var user: FirebaseUser? = null

	private var mDatabase: DatabaseReference? = null
	private var mMessageReference: DatabaseReference? = null
	private var mMessageListener: ChildEventListener? = null

	private var mAdapter: FirebaseRecyclerAdapter<Pin, PinViewHolder>? = null

	private var pinList: MutableList<Pin>? = mutableListOf()
	var recyclerView: RecyclerView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_pin_browsing)

		setSupportActionBar(findViewById(R.id.toolbar))

		mDatabase = FirebaseDatabase.getInstance().reference
		mMessageReference = FirebaseDatabase.getInstance().getReference("PINS")
		user = FirebaseAuth.getInstance().currentUser

		firebaseListenerInit()

		recyclerView = findViewById(R.id.pin_browsing_recycleviewer)

		recyclerView?.layoutManager = LinearLayoutManager(this)

//		val query = mMessageReference

//		mAdapter = object : FirebaseRecyclerAdapter<Pin, PinViewHolder>(
//			Pin::class.java, R.layout.pin_cardview, PinViewHolder::class.java, query
//		) {
//
//			override fun populateViewHolder(
//				viewHolder: PinViewHolder?,
//				model: Pin?,
//				position: Int
//			) {
//				viewHolder?.bindMessage(model)
//			}
//
//			override fun onChildChanged(
//				type: ChangeEventListener.EventType,
//				snapshot: DataSnapshot?,
//				index: Int,
//				oldIndex: Int
//			) {
//				super.onChildChanged(type, snapshot, index, oldIndex)
//				recyclerView?.scrollToPosition(index)
//			}
//		}


		recyclerView?.adapter = sortingAdapter

		sortingAdapter.setOnItemClickListener { item, view ->
			val clickedPin = item as CardViewAdapter
			if (clickedPin.pin.authorID.equals(FirebaseAuth.getInstance().uid)) {
				Toast.makeText(
					this,
					"THATS MY PIN",
					Toast.LENGTH_SHORT
				).show()
				val intent = Intent(view.context, FullMyPinActivity::class.java)
				//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

				startActivity(intent.putExtra("pin", clickedPin.pin))
			} else {
				Toast.makeText(
					this@PinBrowsingActivity,
					"WRONG",
					Toast.LENGTH_SHORT
				).show()

				val intent = Intent(view.context, FullMyPinActivity::class.java)
				startActivity(intent.putExtra("pin", clickedPin.pin))
			}
		}


//		recyclerView?.setOnItemClickListener {
//
//			val clickedPin: Pin =
//				(mAdapter as FirebaseRecyclerAdapter<Pin, PinViewHolder>).getItem(it)
//
//			if (clickedPin.authorID.equals(FirebaseAuth.getInstance().uid)) {
//				Toast.makeText(
//					this@PinBrowsingActivity,
//					"THATS MY PIN",
//					Toast.LENGTH_SHORT
//				).show()
//				val intent = Intent(this@PinBrowsingActivity, FullMyPinActivity::class.java)
//				//intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//				startActivity(intent.putExtra("pin", clickedPin))
//			} else {
//				Toast.makeText(
//					this@PinBrowsingActivity,
//					"WRONG",
//					Toast.LENGTH_SHORT
//				).show()
//
//				val intent = Intent(this, FullMyPinActivity::class.java)
//				startActivity(intent.putExtra("pin", clickedPin))
//			}
//
//		}
	}

	inline fun RecyclerView.setOnItemClickListener(crossinline listener: (position: Int) -> Unit) {
		addOnItemTouchListener(
			RecyclerItemAdapter(
				this,
				object : RecyclerItemAdapter.OnItemClickListener {
					override fun onItemClick(view: View, position: Int) {
						listener(position)
					}
				})
		)
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
					if (pinList!!.elementAt(i).authorID.equals(user?.uid)) {
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
		searchView.setQueryHint("Search View Hint")

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

			override fun onQueryTextChange(newText: String): Boolean {
//				for (i in 0 until pinList!!.size) {
//					if (pinList!![i].contain(newText)) sortingAdapter.add(
//						CardViewAdapter(pinList!![i]))
//				}
				return false
			}

			override fun onQueryTextSubmit(query: String): Boolean {
				sortingAdapter.clear()
				for (i in 0 until pinList!!.size) {
					if (pinList!![i].contain(query)!!) sortingAdapter.add(
						CardViewAdapter(pinList!![i])
					)
				}
				return false
			}
		})
		return true
	}

	private fun firebaseListenerInit() {

		//val childEventListener = object : ChildEventListener {
		mMessageReference?.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)
				pinList?.add(message!!)
				sortingAdapter.add(CardViewAdapter(message!!))
			}

			override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
				val message = dataSnapshot.getValue(Pin::class.java)

			}

			override fun onChildRemoved(dataSnapshot: DataSnapshot) {
				val message = dataSnapshot.getValue(Pin::class.java)
				sortingAdapter.remove(CardViewAdapter(message ?: return))
				pinList?.remove(message)
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

	private fun loginScreen() {
		FirebaseAuth.getInstance().signOut()
		val intent = Intent(this, LoginActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
		finish()
		startActivity(intent)
	}

	override fun onStop() {
		super.onStop()

	}

	override fun onDestroy() {
		super.onDestroy()

		sortingAdapter!!.clear()
	}

}

