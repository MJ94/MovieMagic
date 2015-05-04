package edu.css.mjackson1.moviemagic;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URL;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ResultsActivity extends ActionBarActivity {
    TextView txtResultsTitle;
    TextView plotResults;
    TextView ratingResults;
    ImageView posterResults;

    private class getPlot extends AsyncTask<String, Void, String> {

        String data;
        String rating;
        Bitmap imgDownload;

        @Override
        protected String doInBackground(String... params) {
            String apiURL = "http://www.omdbapi.com/?t="+params[0]+"&y=&plot=short&r=json"; // link to access the API
            String spaceSeparator = "%20"; // this will support spaces in the URL
            apiURL = apiURL.replace(" ",spaceSeparator); // replaces spaces with spaceSeparator (above)

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(apiURL);

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String decrypted = EntityUtils.toString(response.getEntity());

                JSONObject json_data;
                try {
                    json_data = new JSONObject(decrypted);

                    data = json_data.getString("Plot"); // gets the plot data
                    rating = json_data.getString("imdbRating"); // gets the imdb/Rotten Tomatoes rating
                    // code for the poster data
                    String imageURL = json_data.getString("Poster");
                    URL urlImage = new URL(imageURL);
                    imgDownload = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());

                    return "SUCCESS";
                } catch (JSONException e) {
                    return "";
                }

            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String message) {
            // creates a popup if no plot results found
            if(message.isEmpty()){
                Toast.makeText(getApplicationContext(),
                        "Error: No plot found.", Toast.LENGTH_LONG)
                        .show();
            }
            // show data
            else {
                plotResults.setText(data);
                ratingResults.setText(rating);
                posterResults.setImageBitmap(imgDownload);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        txtResultsTitle = (TextView) findViewById(R.id.tvResultsTitle);
        String title = getIntent().getExtras().getString("Title"); // get Title from Main Activity
        txtResultsTitle.setText(title); // sets text to title (see above)

        plotResults = (TextView) findViewById(R.id.tvResultsPlot);
        ratingResults = (TextView) findViewById(R.id.textRating);
        posterResults = (ImageView) findViewById(R.id.posterView);

        getPlot PlotRequestClass = new getPlot();
        PlotRequestClass.execute(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
