package com.stevenhu.android.phone.ui;

//import Content;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stevenhu.android.phone.bean.ADInfo;
import com.stevenhu.android.phone.utils.ViewFactory;

public class MainActivity extends Activity {
	// tab部分
	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2, t3;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;

	// banner图片链接
	private String[] imageUrls = {
			"http://upload.qing5.com/2016/0314/1457921177366.png",
			"http://m4.biz.itc.cn/pic/new/n/27/89/Img8588927_n.jpg",
			"http://static.zhidao.manmankan.com/kimages/201606/21_1466488517119869.jpg",
			"http://assets.jc258.cn/assets/pictures/28894/original_144721387299252000.jpg",
			"http://assets.jc258.cn/assets/pictures/28912/original_144721478440220800.jpg" };

	// listview部分
	private List<Content> contentList = new ArrayList<Content>();
	private ListView left_menu_list;

	// 播放器部分
	public static final String Tag = "MainActivity";
	private ImageButton mStopImageButton;
	private ImageButton mStartImageButton;
	private ImageButton mPauseImageButton;
	//private TextView mTextView;

	private boolean bIsReleased = false;
	private boolean bIsPaused = false;
	private boolean bIsPlaying = false;

	public MediaPlayer mMediaPlayer = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drawerlayout);
		configImageLoader();

		initialize();
		listViewAndClick();
		clickSignButton();
		leftMenuList();
		MusicPlayer();
	//	scollImage();
	//	initTextBar();
	//	InitViewPager();
	}

	// tab初始化
	public void initTextBar() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vpage);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.tab1, null));
		listViews.add(mInflater.inflate(R.layout.tab2, null));
		listViews.add(mInflater.inflate(R.layout.tab3, null));
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void scollImage() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.bar).getWidth();// 图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		public void finishUpdate(View arg0) {

		}

		public int getCount() {
			return mListViews.size();
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 2;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void initContent() {
		Content c1 = new Content("重磅！C罗即将转会巴塞罗那", R.drawable.f,
				"马卡报7.15讯，目前效力于西甲豪门的世界足球先生克里斯蒂亚诺.罗纳尔多已经证实自己转会死敌的消息属实");
		contentList.add(c1);
		Content c2 = new Content("科克：愿成为马竞的杰拉德", R.drawable.b,
				"马竞未来十年的领军人物目前已和西甲劲旅马德里竞技续约，科克表示希望成为杰拉德式的人物");
		contentList.add(c2);
		Content c3 = new Content("格里兹曼：和曼珠基奇合作是荣幸", R.drawable.a,
				"目前欧洲赛场炙手可热的当红炸子鸡格里兹曼在发布会上丝毫不掩饰自己对于克罗地亚前锋的喜爱");
		contentList.add(c3);
		Content c4 = new Content("比利亚告西甲告别战，坚守14年已是传奇", R.drawable.d,
				"马竞当家前锋大卫.比利亚结束了自己在卡尔德隆的最后一场比赛，并将在下载就前往美国大联盟");
		contentList.add(c4);
		Content c5 = new Content("西蒙尼：颠覆王朝的铁血教头", R.drawable.e,
				"马卡报就西蒙尼入主马竞之后的五个赛季进行了一系列的数据分析，结论让人胆寒");
		contentList.add(c5);
		Content c6 = new Content("败走法兰西，斗牛士黄金一带正式落幕", R.drawable.c,
				"在零比二负于孔蒂的意大利后，西班牙黄金一代所建立的王朝正式宣告终结");
		contentList.add(c6);

	}

	// listView及其单击事件
	public void listViewAndClick() {
		initContent();
		ContentAdapter adapter = new ContentAdapter(MainActivity.this,
				R.layout.content, contentList);
		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Content ct = contentList.get(position);
				Toast.makeText(MainActivity.this, ct.getName(),
						Toast.LENGTH_SHORT).show();
				String listName = ct.getName();
				String listContent = ct.getContent();
				int listImageId = ct.getImageID();
				Intent toSecond = new Intent(MainActivity.this,
						ListSecondContent.class);
				toSecond.putExtra("listName", listName);
				toSecond.putExtra("listContent", listContent);
				toSecond.putExtra("ImageId", listImageId);
				startActivity(toSecond);
			}
		});
	}

	@SuppressLint("NewApi")
	private void initialize() {

		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);

		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("图片-->" + i);
			infos.add(info);
		}

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1)
				.getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	private void leftMenuList() {
		left_menu_list = (ListView) findViewById(R.id.left_drawer);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/* 在数组中存放数据 */

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("left_menu_list_image", R.drawable.message1);// 加入图片
		map1.put("left_menu_list_content", "我的通知");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("left_menu_list_image", R.drawable.star);// 加入图片
		map2.put("left_menu_list_content", "我的收藏");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("left_menu_list_image", R.drawable.sys_info);// 加入图片
		map3.put("left_menu_list_content", "系统消息");
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("left_menu_list_image", R.drawable.friends);// 加入图片
		map4.put("left_menu_list_content", "我的好友");
		listItem.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("left_menu_list_image", R.drawable.document);// 加入图片
		map5.put("left_menu_list_content", "资料管理");
		listItem.add(map5);

		// 需要绑定的数据
		// 每一行的布局//动态数组中的数据源的键对应到定义布局的View
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,
				R.layout.left_menu_content, new String[] {
						"left_menu_list_image", "left_menu_list_content" },
				new int[] { R.id.left_menu_list_image,
						R.id.left_menu_list_content });
		left_menu_list.setAdapter(mSimpleAdapter);// 为ListView绑定适配器

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				Toast.makeText(MainActivity.this,
						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
						.show();
			}

		}

	};

	/**
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	private void clickSignButton() {
		ImageButton signupButton = (ImageButton) findViewById(R.id.title_button);
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "you get it",
						Toast.LENGTH_LONG).show();
				Intent signupIntent = new Intent(MainActivity.this,
						SignUp.class);
				startActivity(signupIntent);
			}
		});
	}

	public void MusicPlayer() {
		Log.d(Tag, "music is onCreate");
		mStopImageButton = (ImageButton) findViewById(R.id.StopImageButton);
		mStartImageButton = (ImageButton) findViewById(R.id.StartImageButtom);
		mPauseImageButton = (ImageButton) findViewById(R.id.PauseImageButtom);

		mStopImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				if (bIsPlaying == true) {
					if (!bIsReleased) {
						mMediaPlayer.stop();
						mMediaPlayer.release();
						bIsReleased = true;
					}
					bIsPlaying = false;

				}
			}
		});
		mStartImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				try {
					if (!bIsPlaying) {
						bIsPlaying = true;

						mMediaPlayer = MediaPlayer.create(MainActivity.this,
								R.raw.viva);
						bIsReleased = false;

						mMediaPlayer.setLooping(true);
						try {
							mMediaPlayer.prepare();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mMediaPlayer.start();

					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
				mMediaPlayer
						.setOnCompletionListener(new OnCompletionListener() {
							public void onCompletion(MediaPlayer arg0) {
								mMediaPlayer.release();
							}
						});
			}
		});
		mPauseImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View view) {
				if (mMediaPlayer != null) {
					if (bIsReleased == false) {
						if (bIsPaused == false) {
							mMediaPlayer.pause();
							bIsPaused = true;
							bIsPlaying = true;

						} else if (bIsPaused == true) {
							mMediaPlayer.start();
							bIsPaused = false;

						}
					}
				}
			}
		});

	}
}
