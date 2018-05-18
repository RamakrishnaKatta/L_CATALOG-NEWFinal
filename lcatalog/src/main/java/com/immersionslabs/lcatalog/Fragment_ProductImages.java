package com.immersionslabs.lcatalog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.immersionslabs.lcatalog.Utils.BudgetManager;
import com.immersionslabs.lcatalog.Utils.DownloadManager_3DS;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.Utils.PrefManager;
import com.immersionslabs.lcatalog.Utils.SessionManager;
import com.immersionslabs.lcatalog.Utils.UnzipUtil;
import com.immersionslabs.lcatalog.adapters.ImageSliderAdapter;
import com.immersionslabs.lcatalog.augment.ARNativeActivity;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import static com.immersionslabs.lcatalog.Utils.EnvConstants.user_Favourite_list;

public class Fragment_ProductImages extends Fragment implements OnAnimationEndListener, OnLikeListener, ApiCommunication {

    private static final String TAG = "Fragment_ProductImages";

    private static String FILE_URL_3DS = EnvConstants.APP_BASE_URL + "/upload/web_threeds/";

    private static String LIKE_URL = EnvConstants.APP_BASE_URL + "/users/favouriteArticles";

    private static String EXTENDED_URL_3DS;

    private PrefManager prefManager;

    LinearLayout note;
    ImageButton article_share, article_download, article_3d_view, article_augment, article_budgetlist, article_removelist;
    TextView zip_downloaded;

    String article_images, article_id;
    // article_images is split in to five parts and assigned to each string
    String image1, image2, image3, image4, image5;

    String article_name, article_3ds, article_price;

    String resp, code, message;

    String user_id;
    BudgetManager budgetManager;
//    BudgetListActivity budgetListActivity;

    private ViewPager ArticleViewPager;
    private LinearLayout Slider_dots;
    ImageSliderAdapter imagesliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    TextView Add_Text;
    int page_position = 0;
    int value;

    LikeButton likeButton;

    String Article_3DS_ZipFileLocation, Article_3DS_ExtractLocation, Article_3DS_FileLocation;
    private boolean zip_3ds_downloaded = true;
    File article_3ds_zip_file, article_3ds_file;

    SessionManager sessionmanager;
    String user_log_type;
    private String article_3ds_file_name;

    public Fragment_ProductImages() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_product_images, container, false);

        likeButton = view.findViewById(R.id.article_fav_icon);
        likeButton.setOnLikeListener(this);
        likeButton.setOnAnimationEndListener(this);
        article_share = view.findViewById(R.id.article_share_icon);
        article_download = view.findViewById(R.id.article_download_icon);
        article_3d_view = view.findViewById(R.id.article_3dview_icon);
        article_augment = view.findViewById(R.id.article_augment_icon);
        zip_downloaded = view.findViewById(R.id.download_text);
        article_budgetlist = view.findViewById(R.id.article_budget_icon);
        article_removelist = view.findViewById(R.id.article_remove_icon);
        Add_Text = view.findViewById(R.id.add_text);

        sessionmanager = new SessionManager(getContext());
        HashMap hashmap = new HashMap();
        budgetManager = new BudgetManager();
        hashmap = sessionmanager.getUserDetails();
        user_id = (String) hashmap.get(SessionManager.KEY_USER_ID);
        user_log_type = (String) hashmap.get(SessionManager.KEY_USER_TYPE);

        Log.e(TAG, "User Log Type:  " + user_log_type);

        article_images = getArguments().getString("article_images");
        article_name = getArguments().getString("article_name");
        article_3ds = getArguments().getString("article_3ds");
        article_3ds_file_name = getArguments().getString("article_3ds_file");
        article_id = getArguments().getString("article_id");
        article_price = getArguments().getString("article_new_price");

        Log.d(TAG, "onCreateView:3ds" + article_3ds);
        Log.d(TAG, "onCreateView:3dsfile" + article_3ds_file_name);
        Log.d(TAG, "onCreateView:name" + article_name);

        if (!Objects.equals(user_log_type, "CUSTOMER")) {

            Toast.makeText(getContext(), "This Favourite List is temporary, will be removed after this session", Toast.LENGTH_SHORT).show();

            if (user_Favourite_list.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Exists in the ArrayList");
                likeButton.setLiked(true);
            } else if (!user_Favourite_list.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Doesn't Exist in the ArrayList");
                likeButton.setLiked(false);
            }
        }

        if (Objects.equals(user_log_type, "CUSTOMER")) {

            Set set = sessionmanager.getuserfavoirites();

            if (set.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Exists in the ArrayList");
                likeButton.setLiked(true);
            } else if (!set.contains(article_id)) {
                Log.e(TAG, "Favourite Article List: " + user_Favourite_list + " Article id: " + article_id + "  --Article Doesn't Exist in the ArrayList");
                likeButton.setLiked(false);
            }
        }

        try {
            JSONArray image_json = new JSONArray(article_images);
            for (int i = 0; i < image_json.length(); i++) {
                image1 = image_json.getString(0);
                image2 = image_json.getString(1);
                image3 = image_json.getString(2);
                image4 = image_json.getString(3);
                image5 = image_json.getString(4);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Article Image 1----" + image1);
        Log.e(TAG, "Article Image 2----" + image2);
        Log.e(TAG, "Article Image 3----" + image3);
        Log.e(TAG, "Article Image 4----" + image4);
        Log.e(TAG, "Article Image 5----" + image5);

        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        ArticleViewPager = view.findViewById(R.id.article_view_pager);
        imagesliderAdapter = new ImageSliderAdapter(getContext(), slider_images);
        ArticleViewPager.setAdapter(imagesliderAdapter);

        Slider_dots = view.findViewById(R.id.article_slider_dots);

        ArticleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            private void addBottomDots(int currentPage) {

                dots = new TextView[slider_images.size()];

                Slider_dots.removeAllViews();

                for (int i = 0; i < dots.length; i++) {
                    dots[i] = new TextView(view.getContext());
                    dots[i].setText(Html.fromHtml("&#8226;"));
                    dots[i].setTextSize(35);
                    dots[i].setTextColor(Color.WHITE);
                    Slider_dots.addView(dots[i]);
                }

                if (dots.length > 0)
                    dots[currentPage].setTextColor(Color.parseColor("#004D40"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        Article_3DS_ZipFileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOG/Models/" + article_name + "/" + article_3ds;
//        Log.e(TAG, "ZipFileLocation--" + Article_3DS_ZipFileLocation);
//        Article_3DS_ExtractLocation = Environment.getExternalStorageDirectory() + "/L_CATALOG/Models/" + article_name + "/";
//        Log.e(TAG, "ExtractLocation--" + Article_3DS_ExtractLocation);

        Article_3DS_FileLocation = Environment.getExternalStorageDirectory() + "/L_CATALOG/Models/" + article_name + "/" + article_3ds_file_name;
        Log.e(TAG, "Object3DFileLocation -- " + Article_3DS_FileLocation);

        note = view.findViewById(R.id.download_note);

//        article_3ds_zip_file = new File(Article_3DS_ZipFileLocation);
        article_3ds_file = new File(Article_3DS_FileLocation);

        zip_3ds_downloaded = false;

        article_3d_view.setEnabled(false);
        if (article_3ds_file.exists()) {
            article_3d_view.setEnabled(true);
            article_download.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            zip_3ds_downloaded = true;
            zip_downloaded.setText("File Downloaded");
            zip_downloaded.setTextColor(Color.BLUE);
        }

        article_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Downloading Article, Just for once....");
                progressDialog.setTitle("Article Downloading");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                try {
                                    addModelFolder();

//                                    EXTENDED_URL_3DS = FILE_URL_3DS + article_3ds;
//                                    Log.e(TAG, "URL ---------- " + EXTENDED_URL_3DS);
//                                    new DownloadManager_3DS(EXTENDED_URL_3DS, article_name, article_3ds);
//
//                                    if (article_3ds_zip_file.exists()) {
//                                        new UnzipUtil(Article_3DS_ZipFileLocation, Article_3DS_ExtractLocation);
//                                    } else {
//                                        Toast.makeText(getContext(), "Cannot locate Zipper, Try to download again", Toast.LENGTH_SHORT).show();
//                                    }

                                    EXTENDED_URL_3DS = FILE_URL_3DS + article_3ds_file_name;
                                    Log.e(TAG, "URL ---------- " + EXTENDED_URL_3DS);
                                    new DownloadManager_3DS(EXTENDED_URL_3DS, article_3ds_file_name, article_name);

                                    zip_3ds_downloaded = true;

                                    Log.e(TAG, "Zip Downloaded ---------- " + zip_3ds_downloaded);
                                    progressDialog.dismiss();
                                    article_download.setVisibility(View.GONE);
                                    article_3d_view.setEnabled(true);
                                    note.setVisibility(View.GONE);
                                    zip_downloaded.setText("File Downloaded");
                                    zip_downloaded.setTextColor(getResources().getColor(R.color.primary_dark));

                                } catch (IOException e) {
                                    article_download.setVisibility(View.VISIBLE);
                                    article_3d_view.setEnabled(false);
                                    zip_3ds_downloaded = false;
                                    Log.e(TAG, "Zip Not Downloaded ---------- " + zip_3ds_downloaded);
                                    e.printStackTrace();
                                    note.setVisibility(View.VISIBLE);
                                    zip_downloaded.setText("Download !");
                                }
                            }
                        }, 6000);
            }
        });

        article_3d_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zip_3ds_downloaded == true) {

                    Bundle b3 = new Bundle();
                    b3.putString("article_name", article_name);
                    b3.putString("article_id", article_id);
                    Intent _3d_intent = new Intent(getContext(), Article3dViewActivity.class).putExtras(b3);
                    startActivity(_3d_intent);
                }
            }
        });

        article_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "L_CATALOG");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "If you want to know more details Click here to visit http://immersionslabs.com/ ");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        article_augment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle("You are about to enter Augment Enabled Camera");
                builder.setMessage("This requires 2min of your patience, Do you wish to enter ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getContext(), ARNativeActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        article_budgetlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    HashMap<String, Long> getdetails;
                    getdetails = sessionmanager.getBudgetDetails();
                    Long totalbudget = getdetails.get(SessionManager.KEY_TOTAL_BUDGET_VALUE);
                    if (totalbudget == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Enter Your Budget");

                        final EditText Total_budget_val = new EditText(getContext());

                        Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(Total_budget_val);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String budget_value;
                                budget_value = Total_budget_val.getText().toString();
                                if (budget_value.isEmpty()) {
                                    Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                                } else {
                                    sessionmanager.BUDGET_SET_TOTAL_VALUE(Long.parseLong(budget_value));
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }


                        });
                        builder.show();
                    } else {
                        Long price = Long.parseLong(article_price);
                        Long prevprice = sessionmanager.BUDGET_GET_CURRENT_VALUE();
                        Long temptotalbudget = sessionmanager.BUDGET_GET_TOTAL_VALUE();
                        Long tempcurrentprice = price + prevprice;
                        Long Remaining = temptotalbudget - tempcurrentprice;

                        if (Remaining >= 0) {

                            sessionmanager.BUDGET_ADD_ARTICLE(article_id, price);
                            article_budgetlist.setVisibility(View.GONE);
                            article_removelist.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(), "ADDED TO THE BUDGET LIST", Toast.LENGTH_LONG).show();
                        } else if (Remaining < 0) {
                            Toast.makeText(getContext(), "Budget crossed,try increasing the budget", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (budgetManager.BUDGET_GET_TOTAL() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Enter Your Budget");

                        final EditText Total_budget_val = new EditText(getContext());

                        Total_budget_val.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(Total_budget_val);


                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String budget_value;
                                budget_value = Total_budget_val.getText().toString();

                                if (budget_value.isEmpty()) {
                                    Toast.makeText(getContext(), "Enter a value first", Toast.LENGTH_LONG).show();
                                } else {
                                    budgetManager.BUDGET_SET_TOTAL(Long.parseLong(budget_value));
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    } else {
                        Long price = Long.parseLong(article_price);
                        Long prevprice = budgetManager.BUDGET_GET_CURRENT();
                        Long totalbudget = budgetManager.BUDGET_GET_TOTAL();
                        Long currentprice = price + prevprice;
                        Long Remaining = totalbudget - currentprice;
                        if (Remaining > 0) {
                            budgetManager.BUDGET_SET_CURRENT(currentprice);
                            budgetManager.BUDGET_ADD_ARTICLE(article_id);
                            article_budgetlist.setVisibility(View.GONE);
                            article_removelist.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "ADDED TO THE BUDGET LIST", Toast.LENGTH_LONG).show();
                        }
                        if (Remaining <= 0) {
                            Toast.makeText(getContext(), "Budget crossed,try increasing the budget", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        article_removelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvConstants.user_type.equals("CUSTOMER")) {
                    Long price = Long.parseLong(article_price);
                    sessionmanager.BUDGET_REMOVE_ARTICLE(article_id, price);
                    Toast.makeText(getContext(), "Artcle Removed Successfully", Toast.LENGTH_LONG).show();
                    article_budgetlist.setVisibility(View.VISIBLE);
                    article_removelist.setVisibility(View.GONE);

                } else {
                    Long price = Long.parseLong(article_price);
                    Long prevprice = budgetManager.BUDGET_GET_CURRENT();
                    Long currentprice = prevprice - price;
                    budgetManager.BUDGET_SET_CURRENT(currentprice);
                    budgetManager.BUDGET_REMOVE_ARTICLE(article_id);
                    Toast.makeText(getContext(), "Artcle Removed Successfully", Toast.LENGTH_LONG).show();
                    article_budgetlist.setVisibility(View.VISIBLE);
                    article_removelist.setVisibility(View.GONE);
                }
            }
        });

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (page_position == slider_images.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                ArticleViewPager.setCurrentItem(page_position, true);
            }
        };

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, 2000, 5000);

        prefManager = new PrefManager(getActivity());
        Log.e(TAG, " " + prefManager.ProductPageActivityScreenLaunch());
        if (prefManager.ProductPageActivityScreenLaunch()) {
            ShowcaseView(view);
        }
        return view;
    }

    private void ShowcaseView(View view) {
        prefManager.setProductPageActivityScreenLaunch();
        Log.e(TAG, " " + prefManager.ProductPageActivityScreenLaunch());
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        final TapTargetSequence sequence = new TapTargetSequence(getActivity()).targets(
                TapTarget.forView(view.findViewById(R.id.article_download_icon), "DOWNLOAD", "Click Here before you click the 3d & Augment ")
                        .targetRadius(30)
                        .textColor(R.color.white)
                        .outerCircleColor(R.color.primary_dark)
                        .id(1),
                TapTarget.forView(view.findViewById(R.id.article_augment_icon), "AUGMENT", "Click Here to Augment the Object")
                        .cancelable(false)
                        .textColor(R.color.white)
                        .targetRadius(30)
                        .outerCircleColor(R.color.primary_dark)
                        .id(2),
                TapTarget.forView(view.findViewById(R.id.article_3dview_icon), "3D", "Click Here see the object in 3d View")
                        .cancelable(false)
                        .targetRadius(30)
                        .textColor(R.color.white)
                        .outerCircleColor(R.color.primary_dark)
                        .id(3)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        });
        sequence.start();
    }

    /*creation of directory in external storage */
    private void addModelFolder() throws IOException {
        String state = Environment.getExternalStorageState();

        File folder = null;
        if (state.contains(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "Article Name--" + article_name);
            folder = new File(Environment.getExternalStorageDirectory() + "/L_CATALOG/Models/" + article_name);
        }
        assert folder != null;
        if (!folder.exists()) {
            boolean wasSuccessful = folder.mkdirs();
            Log.e(TAG, "Model Directory is Created --- '" + wasSuccessful + "' Thank You !!");
        }
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
    }

    @Override
    public void liked(LikeButton likeButton) {

        if (Objects.equals(user_log_type, "CUSTOMER")) {
            likeApiCall(1);
            sessionmanager.updateuserfavoirites(article_id);
            Toast.makeText(getContext(), "liked!", Toast.LENGTH_SHORT).show();
        } else if (!Objects.equals(user_log_type, "CUSTOMER")) {
            user_Favourite_list.add(article_id);
            Log.e(TAG, "GUEST FAV LIST" + user_Favourite_list);
            Toast.makeText(getContext(), "liked!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {

        if (Objects.equals(user_log_type, "CUSTOMER")) {
            likeApiCall(0);
            sessionmanager.removeuserfavouirites(article_id);
            Toast.makeText(getContext(), "Disliked!", Toast.LENGTH_SHORT).show();
        } else if (!Objects.equals(user_log_type, "CUSTOMER")) {
            user_Favourite_list.remove(article_id);
            Log.e(TAG, "GUEST FAV LIST" + user_Favourite_list);
            Toast.makeText(getContext(), "Disliked!", Toast.LENGTH_SHORT).show();
        }
    }

    public void likeApiCall(final int value) {
        JSONObject fav_Request = new JSONObject();
        try {
            fav_Request.put("liked", value);
            fav_Request.put("userid", user_id);
            fav_Request.put("article_id", article_id);

            Log.e(TAG, "likeApiCall: UserId-- " + user_id);
            Log.e(TAG, "likeApiCall: Article Id-- " + article_id);
            Log.e(TAG, "Request-- " + fav_Request);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiService.getInstance(getContext()).postData(this, LIKE_URL, fav_Request, "FAVORITE", "FAVORITE_SELECT");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("FAVORITE_SELECT")) {
            Log.e(TAG, "response " + response);
//            if (value == 1) {
//                Toast.makeText(getContext(), "Liked!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "DisLiked!", Toast.LENGTH_SHORT).show();
//            }
            try {
                resp = response.getString("success");
                code = response.getString("status_code");
                message = response.getString("message");

                Log.e(TAG, "Response -- " + response);
                Log.e(TAG, "Response -- " + resp + " \n code-- " + code + " \n message-- " + message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(getContext(), "Internal Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EnvConstants.user_type.equals("GUEST")) {
            if (budgetManager.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                article_budgetlist.setVisibility(View.GONE);
                article_removelist.setVisibility(View.VISIBLE);
            } else {
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
            }

            if (budgetManager.BUDGET_RED_MARKER()) {
                Add_Text.setTextColor(getResources().getColor(R.color.red));
            } else {
                Add_Text.setTextColor(getResources().getColor(R.color.white));
            }
        }
        if (EnvConstants.user_type.equals("CUSTOMER")) {

            if (sessionmanager.BUDGET_IS_ARTICLE_EXISTS(article_id)) {
                article_budgetlist.setVisibility(View.GONE);
                article_removelist.setVisibility(View.VISIBLE);
            } else {
                article_budgetlist.setVisibility(View.VISIBLE);
                article_removelist.setVisibility(View.GONE);
            }

            if (sessionmanager.BUDGET_RED_MARKER()) {
                Add_Text.setTextColor(getResources().getColor(R.color.red));
            } else {
                Add_Text.setTextColor(getResources().getColor(R.color.white));
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}