package view;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.javapapers.android.R;


import network.OmdbService;

public class StructureActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure);

        final OmdbService.Structure structure = getIntent().getParcelableExtra(MOVIE_DETAIL);
        final String imageUrl =  getIntent().getStringExtra(IMAGE_URL);
        Glide.with(this).load(imageUrl).into( (ImageView) findViewById(R.id.main_backdrop));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(structure.Title);

        ((TextView) findViewById(R.id.grid_title)).setText(structure.Title);
        ((TextView) findViewById(R.id.grid_writers)).setText(structure.Writer);
        ((TextView) findViewById(R.id.grid_actors)).setText(structure.Actors);
        ((TextView) findViewById(R.id.grid_director)).setText(structure.Director);
        ((TextView) findViewById(R.id.grid_genre)).setText(structure.Genre);
        ((TextView) findViewById(R.id.grid_released)).setText(structure.Released);
        ((TextView) findViewById(R.id.grid_plot)).setText(structure.Plot);
        ((TextView) findViewById(R.id.grid_runtime)).setText(structure.Runtime);

    }

}
