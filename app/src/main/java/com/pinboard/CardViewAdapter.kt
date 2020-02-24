package com.pinboard


class CardViewAdapter {}//: RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
//
//	//context:Context
//	MainImageUploadInfoList:List<ImageUploadInfo>
//
//	public CardViewAdapter(Context context, List<ImageUploadInfo> TempList)
//	{
//
//		this.MainImageUploadInfoList = TempList;
//
//		this.context = context;
//	}
//
//	override fun onCreateViewHolder(ViewGroup parent, int viewType) {
//
//		View view = LayoutInflater . from (parent.getContext()).inflate(
//			R.layout.recyclerview_items,
//			parent,
//			false
//		);
//
//		ViewHolder viewHolder = new ViewHolder(view);
//
//		return viewHolder;
//	}
//
//	@Override
//	public void onBindViewHolder(ViewHolder holder, int position)
//	{
//		ImageUploadInfo UploadInfo = MainImageUploadInfoList . get (position);
//
//		holder.imageNameTextView.setText(UploadInfo.getImageName());
//
//		//Loading image from Glide library.
//		Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
//	}
//
//	@Override
//	public int getItemCount()
//	{
//
//		return MainImageUploadInfoList.size();
//	}
//
//	class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
//
//		var imageView: ImageView? =null
//		var imageNameTextView: TextView? =null
//
//		public ViewHolder(View itemView)
//		{
//			super(itemView);
//
//			imageView = (ImageView) itemView . findViewById (R.id.pin_cardview);
//
//			imageNameTextView = (TextView) itemView . findViewById (R.id.ImageNameTextView);
//		}
//	}
//
//	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//		val view =
//			LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_cardview, parent, false)
//
//		val viewHolder = ViewHolder(view)
//
//		return viewHolder;
//	}
//
//	override fun getItemCount(): Int {
//		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//	}
//
//	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//	}
//}