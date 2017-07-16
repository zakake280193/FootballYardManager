package ducnm.com.footballyardmanage;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchActivity extends Activity {
    private Boolean exit = false;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.searchBar);
    }






    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
