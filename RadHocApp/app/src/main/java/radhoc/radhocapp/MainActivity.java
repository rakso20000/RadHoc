package radhoc.radhocapp;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

import java.util.Objects;

import radhoc.gamelogic.GameLogicManager;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameType;
import radhoc.radhocapp.databinding.ActivityMainBinding;

public class MainActivity extends ASAPActivity {
	
	private GameStateManager gameStateManager;
	private GameLogicManager gameLogicManager;
	
	private ActivityMainBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RadHocApp app = (RadHocApp) getApplication();
		gameStateManager = Objects.requireNonNull(app.getGameStateManager());
		gameLogicManager = Objects.requireNonNull(app.getGameLogicManager());
		
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Toolbar toolbar = binding.toolbar;
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
		toolBarLayout.setTitle(getTitle());
		
		gameStateManager.createGameState(GameType.TIC_TAC_TOE, "Peter", 1, 1, true);
		
		final String name = gameStateManager.getGameState(1).getOpponentName();
		
		FloatingActionButton fab = binding.fab;
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, name, Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		
	}
}