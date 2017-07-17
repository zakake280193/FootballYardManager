package ducnm.com.footballyardmanage;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends Activity {
    private Boolean exit = false;
    SearchView searchView;
    Spinner spinnerSearch;
    DatabaseReference mDatabase;
    ListView listSearch;
    ArrayList<Yard> listYards = new ArrayList<Yard>();;
    Boolean isData =  false;
    SearchAdapter adapter;
    FloatingActionButton fab;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Order");//groupId, itemId, order, title
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if(item.getTitle()=="Order"){
            Intent intent = new Intent(this, YardActivity.class);
            intent.putExtra("yard", listYards.get(index));
            startActivity(intent);
        }

        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        searchView = (SearchView) findViewById(R.id.searchBar);
        spinnerSearch = (Spinner) findViewById(R.id.spinner);
        listSearch = (ListView) findViewById(R.id.listSearch);
        fab = (FloatingActionButton) findViewById(R.id.fabAddYard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CreateYard.class);
                startActivity(intent);
            }
        });

        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String searchValue = spinnerSearch.getSelectedItem().toString();
                mDatabase.child("Yard").orderByChild("area").endAt(searchValue).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(listYards.size() >0) listYards = new ArrayList<Yard>();
                        if (dataSnapshot.exists()) {
                            isData = true;
                            for (DataSnapshot yardSnapshot : dataSnapshot.getChildren()) {
                                Yard yard = yardSnapshot.getValue(Yard.class);
                                listYards.add(yard);
                            }
                            if(listYards.size() >0) {
                                Toast.makeText(SearchActivity.this, "Number yard " + listYards.size(), Toast.LENGTH_SHORT).show();
                                adapter = new SearchAdapter(SearchActivity.this, R.layout.line_search, listYards);
                                listSearch.setAdapter(adapter);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                if(isData){
//                    adapter = new SearchAdapter(SearchActivity.this, R.layout.line_search, listYards);
//                    listSearch.setAdapter(adapter);
//                }
                isData = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        registerForContextMenu(listSearch);

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
