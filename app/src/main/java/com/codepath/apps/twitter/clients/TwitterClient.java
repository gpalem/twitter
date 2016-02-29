package com.codepath.apps.twitter.clients;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.io.IOException;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "MWQSDsTHlSiP7SSv75tfdOtBM";       // Change this
	public static final String REST_CONSUMER_SECRET = "dwy1F07CFWCu2NV0MkZkjp0uf411LSWxpOl1VMhVGOSOHyO0CA"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://tweetsdisplayapp"; // Change this (here and in manifest)

	private static Context context;
	static AlertDialog networkFailDialog;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
		this.context = context;

		//Network fail dialog
		networkFailDialog = new AlertDialog.Builder(context).setTitle("Network Error").setMessage("Unable to connect to Internet").create();
	}

	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public boolean isOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int     exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (IOException e)          { e.printStackTrace(); }
		catch (InterruptedException e) { e.printStackTrace(); }
		return false;
	}

	public void networkFailHandler(Throwable throwable) {
		try {
			if (isNetworkAvailable() && isOnline()) {
				throwable.printStackTrace();
			}
			else {
				networkFailDialog.show();
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}

	//Get Timeline
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		getClient().get(apiUrl, params, handler);
	}
	public void getNextTweets(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id);
		getClient().get(apiUrl, params, handler);
	}


	//Get Mentions
	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		getClient().get(apiUrl, params, handler);
	}
	public void getNextMentions(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id);
		getClient().get(apiUrl, params, handler);
	}

	//Get User timeline
	public void getUserTimeline(String screeName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screeName);
		getClient().get(apiUrl, params, handler);
	}
	public void getNextUserTimeline(String screeName, long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screeName);
		params.put("max_id", max_id);
		getClient().get(apiUrl, params, handler);
	}

	//Get User info
	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}
	public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, null, handler);
	}

	public void postUpdate(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	//Get Followers
	public void getFollowers(String screeName, long cursor, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		Log.i("FOLLOWERS", screeName + " " + cursor);
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screeName);
		params.put("cursor", cursor);
		getClient().get(apiUrl, params, handler);
	}

	//Get Following
	public void getFollowing(String screeName, long cursor, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		//Specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screeName);
		params.put("cursor", cursor);
		getClient().get(apiUrl, params, handler);
	}
	//Compose Tweet
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}