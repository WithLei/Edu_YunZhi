package com.android.renly.edu_yunzhi.Activity;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Listener.ListItemClickListener;
import com.android.renly.edu_yunzhi.Listener.LoadMoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by free2 on 16-3-6.
 * 单篇文章activity
 * 一楼是楼主
 * 其余是评论
 */
public class PostActivity extends BaseActivity
        implements ListItemClickListener, LoadMoreListener.OnLoadMoreListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private RecyclerView topicList;
    //上一次回复时间
    private long replyTime = 0;
    private int currentPage = 1;
    private int sumPage = 1;
    private int clickPosition = -1;
    private boolean isGetTitle = false;
    private boolean enableLoadMore = false;
    //回复楼主的链接
    private String replyUrl = "";
    private PostAdapter adapter;
    private List<SingleArticleData> datas = new ArrayList<>();
    private boolean isSaveToDataBase = false;
    private String title, authorName, tid, fid, redirectPid = "";
    private boolean showPlainText = false;
    private EditText input;
    private SmileyInputRoot rootView;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;
    private List<String> pageSpinnerDatas = new ArrayList<>();
    private Map<String, String> params;
    private static final Type postListType = new TypeReference<ApiResult<ApiPostList>>() {
    }.getType();

    public static void open(Context context, String url, @Nullable String author) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        intent.putExtra("author", TextUtils.isEmpty(author) ? "null" : author);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initToolBar(true, "加载中......");
        input = findViewById(R.id.ed_comment);
        showPlainText = App.showPlainText(this);
        initCommentList();
        initEmotionInput();
        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        authorName = b.getString("author");
        tid = GetId.getId("tid=", url);

        if (url != null && url.contains("redirect")) {
            redirectPid = GetId.getId("pid=", url);
            if (!App.IS_SCHOOL_NET) {
                url = url + "&mobile=2";
            }
            HttpUtil.head(url, null, new ResponseHandler() {
                @Override
                public void onSuccess(byte[] response) {
                    int page = GetId.getPage(new String(response));
                    firstGetData(page);
                }
            });
        } else {
            firstGetData(1);
        }

        initSpinner();
    }

    private void initCommentList() {
        topicList = findViewById(R.id.topic_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        topicList.setLayoutManager(mLayoutManager);
        adapter = new PostAdapter(this, this, datas);
        topicList.addItemDecoration(new MyListDivider(this, MyListDivider.VERTICAL));
        topicList.addOnScrollListener(new LoadMoreListener(mLayoutManager, this, 8));
        topicList.setAdapter(adapter);
    }

    private void initEmotionInput() {
        View smileyBtn = findViewById(R.id.btn_emotion);
        View btnMore = findViewById(R.id.btn_more);
        View btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        rootView = findViewById(R.id.root);
        rootView.initSmiley(input, smileyBtn, btnSend);
        rootView.setMoreView(LayoutInflater.from(this).inflate(R.layout.my_smiley_menu, null), btnMore);

        topicList.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                KeyboardUtil.hideKeyboard(input);
                rootView.hideSmileyContainer();
            }
            return false;
        });

        MyFriendPicker.attach(this, input);
        findViewById(R.id.btn_star).setOnClickListener(this);
        findViewById(R.id.btn_link).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
    }

    private void initSpinner() {
        spinner = new Spinner(this);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.my_post_spinner_item, pageSpinnerDatas);
        pageSpinnerDatas.add("第1页");
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos + 1 != currentPage) {
                    jumpPage(pos + 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addToolbarView(spinner);
    }

    private void firstGetData(int page) {
        getArticleData(page);
    }

    @Override
    public void onLoadMore() {
        //加载更多被电击
        if (enableLoadMore) {
            enableLoadMore = false;
            if (currentPage < sumPage) {
                currentPage++;
            }
            getArticleData(currentPage);
        }
    }

    public void refresh() {
        adapter.changeLoadMoreState(BaseAdapter.STATE_LOADING);
        //数据填充
        datas.clear();
        adapter.notifyDataSetChanged();
        getArticleData(1);
    }

    //文章一页的html 根据页数 tid
    private void getArticleData(final int page) {
        String url;
        final boolean api = App.IS_SCHOOL_NET;
        if (App.IS_SCHOOL_NET) {
            url = UrlUtils.getArticleApiUrl(tid, currentPage, 20);
        } else {
            url = UrlUtils.getSingleArticleUrl(tid, page, false);
        }

        HttpUtil.get(url, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                //if (api) {
                //    new DealWithArticleDataApi(PostActivity.this).execute(response);
                //} else {
                new DealWithArticleData(PostActivity.this).execute(new String(response));
                //}
            }

            @Override
            public void onFailure(Throwable e) {
                if (e != null && e == SyncHttpClient.NeedLoginError) {
                    isLogin();
                    showToast("此贴需要登录才能查看");
                    return;
                }
                enableLoadMore = true;
                adapter.changeLoadMoreState(BaseAdapter.STATE_LOAD_FAIL);
                showToast("加载失败(Error -1)");
            }
        });
    }

    @Override
    public void onListItemClick(View v, final int position) {
        switch (v.getId()) {
            case R.id.btn_reply_cz:
                if (isLogin()) {
                    SingleArticleData single = datas.get(position);
                    Intent i = new Intent(PostActivity.this, ReplyCzActivity.class);
                    i.putExtra("islz", single.uid.equals(datas.get(0).uid));
                    i.putExtra("data", single);
                    startActivityForResult(i, 20);
                }
                break;
            case R.id.need_loading_item:
                refresh();
                break;
            case R.id.btn_more:
                clickPosition = position;
                PopupMenu popup = new PopupMenu(this, v);
                popup.setOnMenuItemClickListener(this);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_post_more, popup.getMenu());

                //判断是不是自己
                if (!datas.get(position).canManage
                        && (!App.ISLOGIN(this)
                        || !App.getUid(this).equals(datas.get(position).uid))) {
                    popup.getMenu().removeItem(R.id.tv_edit);
                }

                //如果有管理权限，则显示除了关闭之外的全部按钮
                if (!datas.get(position).canManage) {
                    popup.getMenu().removeGroup(R.id.menu_manege);
                }

                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // 更多按钮里面的选项被点击
        switch (menuItem.getItemId()) {
            case R.id.tv_edit:
                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("PID", datas.get(clickPosition).pid);
                i.putExtra("TID", tid);
                startActivityForResult(i, 10);
                break;
            case R.id.tv_remove:
                showDialog("删除帖子!", "请输入删帖理由", "删除", clickPosition, App.MANAGE_TYPE_DELETE);
                break;
            //TODO 处理点击事件
            case R.id.tv_block:
                showDialog("屏蔽帖子！", "请输入屏蔽或者解除", "确定", clickPosition, App.MANAGE_TYPE_BLOCK);
                break;
            case R.id.tv_close:
                showDialog("打开或者关闭主题", "按照格式\n功能(打开/关闭)|yyyy-MM-dd|hh:mm\n"
                                + "填写,例:\n关闭|2018-04-03|04:03\n时间不填为永久",
                        "提交", clickPosition, App.MANAGE_TYPE_CLOSE);
                break;
            case R.id.tv_warn:
                showDialog("警告用户！", "请输入警告或者解除", "确定", clickPosition, App.MANAGE_TYPE_WARN);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                //编辑Activity返回
                Bundle b = data.getExtras();
                String title = b.getString("TITLE", "");
                String content = b.getString("CONTENT", "");
                if (clickPosition == 0 && !TextUtils.isEmpty(title)) {
                    datas.get(0).title = title;
                }
                datas.get(clickPosition).content = content;
                adapter.notifyItemChanged(clickPosition);
            } else if (requestCode == 20) {
                //回复层主返回
                replyTime = System.currentTimeMillis();
                if (currentPage == sumPage) {
                    onLoadMore();
                }
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                replyLz(replyUrl);
                break;
            case R.id.btn_star:
                if (isLogin()) {
                    showToast("正在收藏帖子...");
                    starTask(view);
                }
                break;
            case R.id.btn_link:
                String url = UrlUtils.getSingleArticleUrl(tid, currentPage, false);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, url));
                Toast.makeText(this, "已复制链接到剪切板", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + UrlUtils.getSingleArticleUrl(tid, currentPage, App.IS_SCHOOL_NET));
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到文章到:"));
                break;
        }
    }

    /**
     * 处理数据类 后台进程
     */
    private class DealWithArticleData extends AsyncTask<String, Void, List<SingleArticleData>> {

        private String errorText = "";
        private int pageLoad = 1;
        private Context context;

        DealWithArticleData(Context context) {
            this.context = context;
        }

        @Override
        protected List<SingleArticleData> doInBackground(String... params) {
            errorText = "";
            List<SingleArticleData> tepdata = new ArrayList<>();
            String htmlData = params[0];
            if (!isGetTitle) {
                int ih = htmlData.indexOf("keywords");
                if (ih > 0) {
                    int h_start = htmlData.indexOf('\"', ih + 15);
                    int h_end = htmlData.indexOf('\"', h_start + 1);
                    title = htmlData.substring(h_start + 1, h_end);
                    isGetTitle = true;
                }
            }

            Document doc = Jsoup.parse(htmlData.substring(
                    htmlData.indexOf("<body"),
                    htmlData.lastIndexOf("</body>") + 7));

            Elements as = doc.select(".footer a");
            if (as.size() > 1) {
                String hash = GetId.getHash(as.get(1).attr("href"));
                Log.v("hash", "hash is " + hash);
                App.setHash(PostActivity.this, hash);
            }

            //判断错误
            Elements elements = doc.select(".postlist");
            if (elements.size() <= 0) {
                //有可能没有列表处理错误
                errorText = doc.select(".jump_c").text();
                if (TextUtils.isEmpty(errorText)) {
                    errorText = "network error  !!!";
                }
                return tepdata;
            }

            //获得回复楼主的url
            if (TextUtils.isEmpty(replyUrl)) {
                String s = elements.select("form#fastpostform").attr("action");
                if (!TextUtils.isEmpty(s)) {
                    replyUrl = s;
                    //获得板块ID用于请求数据
                    fid = GetId.getId("fid=", replyUrl);
                }
            }

            //获取总页数 和当前页数
            if (doc.select(".pg").text().length() > 0) {
                if (doc.select(".pg").text().length() > 0) {
                    pageLoad = GetId.getNumber(doc.select(".pg").select("strong").text());
                    int n = GetId.getNumber(doc.select(".pg").select("span").attr("title"));
                    if (n > 0 && n > sumPage) {
                        sumPage = n;
                    }
                }
            }

            Elements postlist = elements.select("div[id^=pid]");
            int size = postlist.size();
            for (int i = 0; i < size; i++) {
                Element temp = postlist.get(i);
                String pid = temp.attr("id").substring(3);
                String uid = GetId.getId("uid=", temp.select("span[class=avatar]").select("img").attr("src"));
                Elements userInfo = temp.select("ul.authi");
                // 手机版commentIndex拿到的原始数据是"楼层 管理"
                String commentIndex = userInfo.select("li.grey").select("em").first().text();
                String username = userInfo.select("a[href^=home.php?mod=space&uid=]").text();
                boolean canManage = false;
                // 判别是否对该帖子是否有管理权限
                if (App.ISLOGIN(context)) {
                    if (App.IS_SCHOOL_NET) {
                        // 校园网
                        Elements es = temp.select("div.plc.cl").select("div.display.pi")
                                .select("ul.authi").select("li.grey.rela").select("em");
                        if (es != null && es.size() != 0) {
                            canManage = es.first()
                                    .select("a").text().equals("管理");

                        }
                    } else {
                        // 校外网
                        Elements es = userInfo.select("li.grey.rela").select("em");
                        if (es != null && es.size() != 0) {
                            canManage = es.first()
                                    .select("a").text().equals("管理");
                        }
                    }
                }
                // 手机版postTime拿到的原始数据是"管理 收藏 时间"
                String postTime = userInfo.select("li.grey.rela").text()
                        .replace("收藏", "")
                        .replace("管理", "");
                String replyUrl = temp.select(".replybtn").select("input").attr("href");
                Elements contentels = temp.select(".message");

                //去除script
                contentels.select("script").remove();

                //是否移除所有样式
                if (showPlainText) {
                    //移除所有style
                    contentels.select("[style]").removeAttr("style");
                    contentels.select("font").removeAttr("color").removeAttr("size").removeAttr("face");
                }

                //处理代码
                for (Element codee : contentels.select(".blockcode")) {
                    codee.html("<code>" + codee.html().trim() + "</code>");
                }

                //处理引用
                for (Element codee : contentels.select("blockquote")) {
                    int start = codee.html().indexOf("发表于");
                    if (start > 0) {
                        Elements es = codee.select("a");
                        if (es.size() > 0 && es.get(0).text().contains("发表于")) {
                            String user = es.get(0).text().substring(0, es.get(0).text().indexOf(" "));
                            int sstart = codee.html().indexOf("<br>", start) + 4;
                            codee.html(user + ":" + codee.html().substring(sstart).replaceAll("<br>", " "));
                            break;
                        }
                    }
                }


                SingleArticleData data;
                if (pageLoad == 1 && i == 0) {//内容
                    //处理投票
                    VoteData d = null;
                    int maxSelection = 1;
                    Elements vote = contentels.select("form[action^=forum.php?mod=misc&action=votepoll]");
                    if (vote.size() > 0 && vote.select("input[type=submit]").size() > 0) {// 有且有投票权
                        if (vote.text().contains("单选投票")) {
                            maxSelection = 1;
                        } else if (vote.text().contains("多选投票")) {
                            int start = vote.text().indexOf("多选投票");
                            maxSelection = GetId.getNumber(vote.text().substring(start, start + 20));
                        }

                        Elements ps = vote.select("p");
                        List<Pair<String, String>> options = new ArrayList<>();
                        for (Element p : ps) {
                            if (p.select("input").size() > 0)
                                options.add(new Pair<>(p.select("input").attr("value"),
                                        p.select("label").text()));
                        }

                        if (ps.select("input").get(0).attr("type").equals("radio")) {
                            maxSelection = 1;
                        }

                        vote.select("input[type=submit]").get(0).html("<a href=\"" +
                                LinkClickHandler.VOTE_URL + "\">点此投票</a><br>");
                        d = new VoteData(vote.attr("action"), options, maxSelection);
                    }
                    data = new SingleArticleData(SingleType.CONTENT, title, uid,
                            username, postTime,
                            commentIndex, replyUrl, contentels.html().trim(), pid, canManage);
                    data.vote = d;
                    authorName = username;
                    if (!isSaveToDataBase) {
                        //插入数据库
                        MyDB myDB = new MyDB(PostActivity.this);
                        myDB.handSingleReadHistory(tid, title, authorName);
                        isSaveToDataBase = true;
                    }
                } else {//评论
                    data = new SingleArticleData(SingleType.COMMENT, title, uid,
                            username, postTime, commentIndex, replyUrl,
                            contentels.html().trim(), pid, canManage);
                }
                tepdata.add(data);
            }
            return tepdata;
        }

        @Override
        protected void onPostExecute(List<SingleArticleData> tepdata) {
            enableLoadMore = true;
            if (isGetTitle) {
                setTitle("帖子正文");
            }

            if (pageLoad != currentPage) {
                currentPage = pageLoad;
                spinner.setSelection(currentPage - 1);
            }

            if (!TextUtils.isEmpty(errorText)) {
                Toast.makeText(PostActivity.this, errorText, Toast.LENGTH_SHORT).show();
                adapter.changeLoadMoreState(BaseAdapter.STATE_LOAD_FAIL);
                return;
            }
            if (tepdata.size() == 0) {
                adapter.changeLoadMoreState(BaseAdapter.STATE_LOAD_NOTHING);
                return;
            }

            int startsize = datas.size();
            if (datas.size() == 0) {
                datas.addAll(tepdata);
            } else {
                String strindex = datas.get(datas.size() - 1).index;
                if (TextUtils.isEmpty(strindex)) {
                    strindex = "-1";
                } else if (strindex.equals("沙发")) {
                    strindex = "1";
                } else if (strindex.equals("板凳")) {
                    strindex = "2";
                } else if (strindex.equals("地板")) {
                    strindex = "3";
                }
                int index = GetId.getNumber(strindex);
                for (int i = 0; i < tepdata.size(); i++) {
                    String strindexp = tepdata.get(i).index;
                    if (strindexp.equals("沙发")) {
                        strindexp = "1";
                    } else if (strindex.equals("板凳")) {
                        strindexp = "2";
                    } else if (strindex.equals("地板")) {
                        strindexp = "3";
                    }
                    int indexp = GetId.getNumber(strindexp);
                    if (indexp > index) {
                        datas.add(tepdata.get(i));
                    }
                }
            }

            if (currentPage < sumPage) {
                adapter.changeLoadMoreState(BaseAdapter.STATE_LOADING);
            } else {
                adapter.changeLoadMoreState(BaseAdapter.STATE_LOAD_NOTHING);
            }

            if (datas.size() > 0 && (datas.get(0).type != SingleType.CONTENT) &&
                    (datas.get(0).type != SingleType.HEADER)) {
                datas.add(0, new SingleArticleData(SingleType.HEADER, title,
                        null, null, null, null, null, null, null));
            }
            int add = datas.size() - startsize;
            if (startsize == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(startsize, add);
            }

            //打开的时候移动到指定楼层
            if (!TextUtils.isEmpty(redirectPid)) {
                for (int i = 0; i < datas.size(); i++) {
                    if (!TextUtils.isEmpty(datas.get(i).pid)
                            && datas.get(i).pid.equals(redirectPid)) {
                        topicList.scrollToPosition(i);
                        break;
                    }
                }
                redirectPid = "";
            }

            int size = pageSpinnerDatas.size();
            if (sumPage != size) {
                pageSpinnerDatas.clear();
                for (int i = 1; i <= sumPage; i++) {
                    pageSpinnerDatas.add("第" + i + "页");
                }
                spinnerAdapter.notifyDataSetChanged();
                spinner.setSelection(currentPage - 1);
            }
        }

    }

    /**
     * 处理数据类（API） 后台进程
     */
    private class DealWithArticleDataApi extends AsyncTask<byte[], Void, List<SingleArticleData>> {

        private Context context;
        private String errorText = "";
        private int pageLoad = 1;


        DealWithArticleDataApi(Context context) {
            this.context = context;
        }

        @Override
        protected List<SingleArticleData> doInBackground(byte[]... params) {
            ApiResult<ApiPostList> res = JSON.parseObject(params[0], postListType);
            if (res == null) {
                errorText = "没有获取到文章内容";
                return null;
            }
            if (!isGetTitle) {
                title = res.Variables.thread.subject;
                isGetTitle = true;
            }

            App.setHash(context, res.Variables.formhash);
            fid = res.Variables.fid;
            tid = res.Variables.thread.tid;
            authorName = res.Variables.thread.author;


            //todo replyUrl
            //获取总页数 和当前页数
            //todo  pageLoad  sumPage
            List<Postlist> postlist = res.Variables.postlist;
            List<SingleArticleData> tempDatas = new ArrayList<>(res.Variables.ppp);
            int size = postlist.size();
            for (int i = 0; i < size; i++) {
                Postlist temp = postlist.get(i);
                String pid = temp.pid;
                String uid = temp.authorid;

                //TODO
                String commentIndex = "1";
                String username = temp.author;

                //TODO
                boolean canManage = false;

                String postTime = temp.dateline;

                //TODO
                String replyUrl = null;
                String content = temp.message;

                //是否移除所有样式
                if (showPlainText) {
                    //TODO
                }

                SingleArticleData data;
                if (temp.first == 1) { //内容
                    data = new SingleArticleData(SingleType.CONTENT, title, uid,
                            username, postTime,
                            commentIndex, replyUrl, content, pid, canManage);

                    //TODO vote
                    //data.vote =
                    if (!isSaveToDataBase) {
                        //插入数据库
                        MyDB myDB = new MyDB(PostActivity.this);
                        myDB.handSingleReadHistory(tid, title, authorName);
                        isSaveToDataBase = true;
                    }
                } else {
                    data = new SingleArticleData(SingleType.COMMENT, title, uid,
                            username, postTime, commentIndex, replyUrl, content, pid, canManage);
                }


                tempDatas.add(data);
            }
            return tempDatas;
        }

        @Override
        protected void onPostExecute(List<SingleArticleData> dataset) {

        }
    }

    /**
     * 收藏帖子
     */
    private void starTask(final View v) {
        final String url = UrlUtils.getStarUrl(tid);
        Map<String, String> params = new HashMap<>();
        params.put("favoritesubmit", "true");
        HttpUtil.post(url, params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                if (res.contains("成功") || res.contains("您已收藏")) {
                    showToast("收藏成功");
                    if (v != null) {
                        final ImageView mv = (ImageView) v;
                        mv.postDelayed(() -> mv.setImageResource(R.drawable.ic_star_32dp_yes), 300);
                    }
                }
            }
        });
    }


    private void startBlock(int position) {
        String url = "forum.php?mod=topicadmin&action=banpost"
                + "&fid=" + fid
                + "&tid=" + tid
                + "&topiclist[]=" + datas.get(position).pid
                + "&mobile=2&inajax=1";
        params = null;
        HttpUtil.get(url, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                Document document = RuisUtils.getManageContent(response);
                params = RuisUtils.getForms(document, "topicadminform");
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误！请重试");
            }
        });
    }

    private void startWarn(int position) {
        if (App.IS_SCHOOL_NET) {
            // computer
            params = new HashMap<>();
            params.put("fid", fid);
            params.put("page", "1");
            params.put("tid", tid);
            params.put("handlekey", "mods");
            params.put("topiclist[]", datas.get(position).pid);
            params.put("reason", " 手机版主题操作");
        } else {
            String url = "forum.php?mod=topicadmin&action=warn&fid=" + fid
                    + "&tid=" + tid
                    + "&operation=&optgroup=&page=&topiclist[]=" + datas.get(position).pid + "&mobile=2&inajax=1";
            //url = forum.php?mod=topicadmin&action=warn&fid=72&tid=922824&handlekey=mods&infloat=yes&nopost=yes&r0.8544855790245922&inajax=1
            params = null;
            HttpUtil.get(url, new ResponseHandler() {
                @Override
                public void onSuccess(byte[] response) {
                    Document document = getManageContent(response);
                    params = RuisUtils.getForms(document, "topicadminform");
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);
                }
            });
        }
    }

    private void startClose() {
        String url = "";
        if (App.IS_SCHOOL_NET) {
            url = "forum.php?mod=topicadmin&action=moderate&fid=" + fid + "&moderate[]=" + tid + "&handlekey=mods" +
                    "&infloat=yes&nopost=yes&from=" + tid + "&inajax=1";
        } else {
            url = "forum.php?mod=topicadmin&action=moderate&fid=" + fid
                    + "&moderate[]=" + tid + "&from=" + tid
                    + "&optgroup=4&mobile=2";
        }
        params = null;
        HttpUtil.get(url, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                Document document = RuisUtils.getManageContent(response);
                params = RuisUtils.getForms(document, "moderateform");
                params.put("redirect", "");
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误！请重试");
            }
        });
    }

    private void warnUser(int position, String s) {
        if (s.equals("警告")) {
            params.put("warned", "1");
        } else {
            params.put("warned", "0");
        }
        HttpUtil.post(UrlUtils.getWarnUserUrl(), params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                if (res.contains("成功")) {
                    showToast("帖子操作成功，刷新帖子即可看到效果");
                } else {
                    showToast("管理操作失败,我也不知道哪里有问题");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误，操作失败！");
            }
        });
    }

    private void blockReply(int position, String s) {
        if (s.equals("屏蔽")) {
            params.put("banned", "1");
        } else {
            params.put("banned", "0");
        }
        HttpUtil.post(UrlUtils.getBlockReplyUrl(), params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                if (res.contains("成功")) {
                    showToast("帖子操作成功，刷新帖子即可看到效果");
                } else {
                    showToast("管理操作失败,我也不知道哪里有问题");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误，操作失败！");
            }
        });
    }

    // 打开或者关闭帖子
    private void closeArticle(String[] str) {
        if (str.length == 3) {
            params.put("expirationclose", str[1] + " " + str[2]);
        } else {
            params.put("expirationclose", "");
        }
        if (str[0].equals("打开")) {
            params.put("operations[]", "open");
        } else if (str[0].equals("关闭")) {
            params.put("operations[]", "close");
        }
        params.put("reason", "手机版主题操作");
        HttpUtil.post(UrlUtils.getCloseArticleUrl(), params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                if (res.contains("成功")) {
                    showToast(str[0] + "帖子操作成功，刷新帖子即可看到效果");
                } else {
                    showToast("管理操作失败,我也不知道哪里有问题");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误，" + str[0] + "帖子失败！");
            }
        });
    }

    private void startDelete(int position) {
        if (App.IS_SCHOOL_NET) {
            params = new HashMap<>();
            params.put("fid", fid);
            params.put("handlekey", "mods");
            if (datas.get(position).type == SingleType.CONTENT) {
                // 主题
                params.put("moderate[]", tid);
                params.put("operations[]", "delete");
            } else {
                // 评论
                params.put("topiclist[]", datas.get(position).pid);
                params.put("tid", tid);
                params.put("page", (1 + position / 10) + "");
            }
        } else {
            String url;
            // 以下仅仅针对手机版做了测试
            if (datas.get(position).type == SingleType.CONTENT) {
                //删除整个帖子
                url = "forum.php?mod=topicadmin&action=moderate&fid=" + fid
                        + "&moderate[]=" + tid + "&operation=delete&optgroup=3&from="
                        + tid + "&mobile=2&inajax=1";
            } else {
                //删除评论
                url = "forum.php?mod=topicadmin&action=delpost&fid=" + fid
                        + "&tid=" + tid + "&operation=&optgroup=&page=&topiclist[]="
                        + datas.get(position).pid + "&mobile=2&inajax=1";
            }
            params = null;
            HttpUtil.get(url, new ResponseHandler() {
                @Override
                public void onSuccess(byte[] response) {
                    Document document = RuisUtils.getManageContent(response);
                    if (datas.get(position).type == SingleType.CONTENT) {
                        params = RuisUtils.getForms(document, "moderateform");
                    } else {
                        params = RuisUtils.getForms(document, "topicadminform");
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);
                    showToast("网络错误！");
                }
            });
        }
    }

    //删除帖子或者回复
    private void removeItem(final int pos, String reason) {
        params.put("redirect", "");
        params.put("reason", reason);
        HttpUtil.post(UrlUtils.getDeleteReplyUrl(datas.get(pos).type), params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                Log.e("result", res);
                if (res.contains("成功")) {
                    if (datas.get(pos).type == SingleType.CONTENT) {
                        showToast("主题删除成功");
                        finish();
                    } else {
                        showToast("回复删除成功");
                        datas.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                } else {
                    int start = res.indexOf("<p>");
                    int end = res.indexOf("<", start + 5);
                    String ss = res.substring(start + 3, end);
                    showToast(ss);
                }

            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);
                showToast("网络错误,删除失败！");
            }
        });
    }

    //回复楼主
    private void replyLz(String url) {
        if (!(isLogin() && checkTime() && checkInput())) {
            return;
        }
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("回复中");
        dialog.setMessage("请稍后......");
        dialog.show();

        String s = getPreparedReply(this, input.getText().toString());
        Map<String, String> params = new HashMap<>();
        params.put("message", s);
        HttpUtil.post(url + "&handlekey=fastpost&loc=1&inajax=1", params, new ResponseHandler() {
            @Override
            public void onSuccess(byte[] response) {
                String res = new String(response);
                handleReply(true, res);
            }

            @Override
            public void onFailure(Throwable e) {
                handleReply(false, "");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }


    public static String getPreparedReply(Context context, String text) {
        int len = 0;
        try {
            len = text.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        if (shp.getBoolean("setting_show_tail", false)) {
            String texttail = shp.getString("setting_user_tail", "无尾巴").trim();
            if (!texttail.equals("无尾巴")) {
                texttail = "     " + texttail;
                text += texttail;
            }
        }

        //字数补齐补丁
        if (len < 13) {
            int need = 14 - len;
            for (int i = 0; i < need; i++) {
                text += " ";
            }
        }

        return text;
    }

    private void handleReply(boolean isok, String res) {
        if (isok) {
            if (res.contains("成功") || res.contains("层主")) {
                Toast.makeText(this, "回复发表成功", Toast.LENGTH_SHORT).show();
                input.setText(null);
                replyTime = System.currentTimeMillis();
                KeyboardUtil.hideKeyboard(input);
                rootView.hideSmileyContainer();
                if (sumPage == 1) {
                    refresh();
                } else if (currentPage == sumPage) {
                    onLoadMore();
                }
            } else if (res.contains("您两次发表间隔")) {
                showToast("您两次发表间隔太短了......");
            } else if (res.contains("主题自动关闭")) {
                showLongToast("此主题已关闭回复,无法回复");
            } else {
                showToast("由于未知原因发表失败");
            }
        } else {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
        }
    }

    //跳页
    private void jumpPage(int to) {
        datas.clear();
        adapter.notifyDataSetChanged();
        getArticleData(to);
    }

    private boolean checkInput() {
        String s = input.getText().toString();
        if (TextUtils.isEmpty(s)) {
            showToast("你还没写内容呢!");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTime() {
        if (System.currentTimeMillis() - replyTime > 15000) {
            return true;
        } else {
            showToast("还没到15s呢，再等等吧!");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!rootView.hideSmileyContainer()) {
            super.onBackPressed();
        }
    }

    //显示投票dialog
    public void showVoteView() {
        if (datas.get(0).type == SingleType.CONTENT) {
            VoteData d = datas.get(0).vote;
            if (d != null) {
                VoteDialog.show(this, d);
                return;
            }

        }
        showToast("投票数据异常无法投票");
    }

    //用户点击了回复链接
    //显示软键盘
    public void showReplyKeyboard() {
        KeyboardUtil.showKeyboard(input);
    }

    // 管理按钮点击后的确认窗口
    public void showDialog(String title, String message, String posStr,
                           int position, int type) {
        final EditText edt = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("取消", null)
                .setView(edt)
                .setCancelable(true);
        switch (type) {
            case App.MANAGE_TYPE_EDIT:
                // nothing to do
                break;
            case App.MANAGE_TYPE_DELETE:
                builder.setPositiveButton(posStr, (dialog, which) -> {
                    if (!edt.getText().toString().equals("")) {
                        removeItem(position, edt.getText().toString());
                    } else {
                        showToast("请输入删帖理由!");
                    }
                });
                startDelete(position);
                break;
            case App.MANAGE_TYPE_BLOCK:
                builder.setPositiveButton(posStr, (dialog, which) -> {
                    if (!edt.getText().toString().equals("")
                            && (edt.getText().toString().equals("屏蔽")
                            || edt.getText().toString().equals("解除"))) {
                        blockReply(position, edt.getText().toString());
                    } else {
                        showToast("请输入屏蔽或者解除");
                    }
                });
                startBlock(position);
                break;
            case App.MANAGE_TYPE_WARN:
                builder.setPositiveButton(posStr, (dialog, which) -> {
                    if (!edt.getText().toString().equals("")
                            && (edt.getText().toString().equals("警告")
                            || edt.getText().toString().equals("解除"))) {
                        warnUser(position, edt.getText().toString());
                    } else {
                        showToast("请输入警告或者解除");
                    }
                });
                startWarn(position);
                break;
            case App.MANAGE_TYPE_CLOSE:
                builder.setPositiveButton(posStr, (dialog, which) -> {
                    String[] time = edt.getText().toString().split("\\|");
                    if (("打开".equals(time[0]) || "关闭".equals(time[0])
                            && (time.length == 1 || time.length == 3))) {
                        closeArticle(time);
                    } else {
                        showToast("输入格式错误，请重新输入");
                    }
                });
                startClose();
                break;
            default:
                break;
        }
        builder.create().show();
    }


}

