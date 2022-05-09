# HifiNi

#### 介绍

爬取[HiFiNi](https://www.hifini.com/)网站资源。

**那些需要VIP的、正常软件上没有的音乐，试试这个。**

#### 功能

1. 分类音乐数据显示。
2. 搜索音乐。
3. 播放。
4. 下载。

目前下载的都是试听版本的（m4a,mp3），高质量flac的需要回复百度网盘下载，后续看看这部分能不能做。

### 下载地址

![HiFiNi](https://qr.api.cli.im/newqr/create?data=http%3A%2F%2Ffile.cudag.com%2F2022%2F01%2F13%2F503c115140ab17b1809dd646318f34f4.apk&level=H&transparent=false&bgcolor=%23FFFFFF&forecolor=%23000&blockpixel=12&marginblock=2&logourl=&logoshape=no&size=191&bgimg=&text=&fontsize=30&fontcolor=&fontfamily=msyh.ttf&incolor=%231694e3&outcolor=&qrcode_eyes=pin-3.png&background=images%2Fbackground%2Fbg25.png&wper=0.84&hper=0.84&tper=0.08&lper=0.08&eye_use_fore=&qrpad=10&embed_text_fontfamily=simhei.ttc&body_type=0&qr_rotate=0&logo_pos=0&kid=cliim&key=0ae96de8ad8c03201262c0d033109119 "HifiNi-Download.png")

#### 感谢

[Jetpack-MusicPlayer](https://github.com/KunMinX/Jetpack-MusicPlayer)




#### 数据库

数据库使用Jetpack-Room框架 [Room的使用](https://www.jianshu.com/p/cb65fd4c94e9)
```gradle
def room_version = "2.3.0"
implementation "androidx.room:room-ktx:$room_version"
kapt "androidx.room:room-compiler:$room_version"
```
共有三张表

1. [User](https://gitee.com/theoneee/hifi-ni/blob/dev/app/src/main/java/com/theone/music/data/model/User.java)  

   用户信息表，用于用户系统的本地保存，`Music`表的关联。

   主要用于登录注册界面，详情见代码：[LoginRegisterViewModel](https://gitee.com/theoneee/hifi-ni/blob/dev/app/src/main/java/com/theone/music/viewmodel/LoginRegisterViewModel.kt)

2.  [Music](https://gitee.com/theoneee/hifi-ni/blob/dev/app/src/main/java/com/theone/music/data/model/Music.java)	

   这张表使用`sharUrl`为唯一键，一个分享视为一首歌曲信息。

   ```java
   音乐的分享链接：https://hifini.com/thread-35837.htm
   
   sharUrl = thread-35837.htm
   ```

   首次根据分享的链接得到音乐数据进行一次保存

   ```
   // 请求 https://hifini.com/thread-35837.htm
   // response 其实一个html 信息
   // 我们最后想要拿到的是一个ListBean
   val response = request(link)
   // 解析
   val music = parseMusicInfo(response).apply {
       shareUrl = link
       if (!url.startsWith("http")) {
           // 然后得到重定向后的地址
           realUrl = getRedirectUrl(url)
       }
   }
   music.createDate = System.currentTimeMillis()
   // 保存到数据库
   MUSIC_DAO.insert(music)
   
   ```

   然后就可以在每次进入播放界面时，首先判断数据库里面是否有当前分享链接的播放地址，如果有，直接拿来播放

   ```java
   // TODO Step3 查询DB
   Music dbMusic = getViewModel().requestDbMusic();
   if (null != dbMusic) {
       String cacheUrl = playerManager.getCacheUrl(dbMusic.getMusicUrl());
       getViewModel().setMediaSource(dbMusic, user, !cacheUrl.isEmpty());
   }
   ```

   * 收藏操作

   ```java
   /**
   * 更新用户收藏音乐
   * @param userId 用户id
   * @param shareUrl 分享的Url
   * @param collection 收藏状态
   * @param createDate 时间
   * @return int 更新结果
   */
   @Query("update MusicInfo set userId = :userId,collection = :collection, createDate =:createDate  where shareUrl ==:shareUrl")
   int updateCollectionMusic(int userId,String shareUrl,int collection,long createDate);
   ```

   收藏之前唯一的判断就是当前是否有登录的用户，因为收藏需要存入对应的用户ID，也就是登录了才能对收藏进行操作

   ```java
    // 用户登录、退出后的消息
    mEvent.getUserInfoLiveData().observe(this, new Observer<User>() {
    	@Override
    	public void onChanged(User user) {
    		// 是否可以点击收藏
    		boolean isLogin = user != null;
    		mMusicViewModel.isCollectionEnable().set(isLogin);
    		// 先默认设置为false
    		mMusicViewModel.isCollection().set(false);
    		// 登录后当前有播放，查询当前用户是否已收藏当前的播放
    		if (isLogin) {
    			Music music = getCurrentMusic();
    			if (null != music) {
    				mMusicViewModel.requestCollection(user, music.shareUrl);
    			}
    		}
    	}
    });
   ```

   * 每次播放后都更新最后播放时间

   ```java
   /**
   * 更新音乐的最后播放日期 - 播放记录
   * @param date 日期
   * @param shareUrl 分享地址
   * @return 更新状态
   */
   @Query("update MusicInfo set lastPlayDate = :date where shareUrl ==:shareUrl")
   int updateMusicLastPlayDate(long date,String shareUrl);
   ```

3. [Download](https://gitee.com/theoneee/hifi-ni/blob/dev/app/src/main/java/com/theone/music/data/model/Download.java)

   下载这里没有对用户进行关联，因为下载后的文件在本地，暂时属于任何人都能使用的范围。

   主要是对音乐表的关联：

   ```java
   // 数据库里拿到MusicId
   List<Music> musics = DataRepository.Companion.getMUSIC_DAO().getMusicsByShareUrl(mDownload.shareUrl);
   musicId = musics.get(0).id;
   // 创建一个下载对象
   Download download = new Download(musicId, downloadPath + File.separator + name, DownloadStatus.DOWNLOADING, System.currentTimeMillis());
   // 添加到数据库
   DataRepository.Companion.getDOWNLOAD_DAO().insert(download);
   ```

   然后就是查询

   ```kotlin
   val response = mutableListOf<DownloadResult>()
   //  TODO 1 首先查询到下载数据
   val downloads = DataRepository.DOWNLOAD_DAO.getDownloadList(page,10)
   // 然后遍历
   for (download in downloads){
       // TODO 2 通过musicId得到Music信息
       val musics = DataRepository.MUSIC_DAO.getMusicById(download.musicId)
       // 下载成功后判断是否文件存在
       if(download.status == DownloadStatus.SUCCESS){
           if(!File(download.localPath).exists()){
               download.status = DownloadStatus.FILE_DELETE
           }
       }
       // 更改格式
       response.add(DownloadResult().apply {
           music = musics[0]
           localPath = download.localPath
           time  = download.time
           status = download.status
       })
   }
   ```

   这里Step2应该是可以通过Step1通过一个SQL语句连带查询出来的，这里我不会只能这样写个笨方法了。






