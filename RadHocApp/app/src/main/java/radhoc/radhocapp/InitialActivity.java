package radhoc.radhocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAP;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import radhoc.communication.Communication;
import radhoc.communication.CommunicationFactory;
import radhoc.communication.MockCommunication;
import radhoc.gamelogic.GameLogicManager;
import radhoc.gamelogic.GameLogicManagerFactory;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateManagerFactory;
import radhoc.invitations.InvitationManager;
import radhoc.invitations.InvitationManagerFactory;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class InitialActivity extends AppCompatActivity {
	
	private static final String PREFERENCES_FILE = "Radhoc_SharedPreferences";
	private static final String SHARK_OWNER_ID = "RadHoc_SharkOwnerID";
	private static final String USER_ID = "RadHoc_UserID";
	private static final String USERNAME = "RadHoc_Username";
	private static final String ASAP_FOLDER_NAME = "RadHoc_ASAPData";
	
	private RadHocApp app;
	
	private Optional<String> sharkOwnerID;
	private Optional<Long> userID;
	private Optional<String> username;
	
	private SharkPeer sharkPeer;
	private Future<Communication> communicationFuture;
	
	public InitialActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (RadHocApp) getApplication();
		
		if (app.isInitialized()) {
			
			done();
			return;
			
		}
		
		boolean preferencesReady = preparePreferences();
		
		if (preferencesReady)
			initialize();
		
	}
	
	private boolean preparePreferences() {
		
		readPreferences();
		
		if (!(sharkOwnerID.isPresent() && userID.isPresent() && username.isPresent()))
			return generatePreferences();
		
		return true;
		
	}
	
	private void initialize() {
		
		createSharkPeer();
		
		try {
			
			createCommunicationFuture();
			startSharkPeer();
			createComponents();
			
			done();
			
		} catch (SharkException | IOException | InterruptedException | ExecutionException e) {
			
			Log.e("RadHoc InitialActivity", "Fatal error trying to initialize RadHoc: " + e.getMessage());
			
			Toast.makeText(this, "Fatal error trying to initialize RadHoc", Toast.LENGTH_LONG).show();
			
		}
		
	}
	
	private void done() {
		
		finish();
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
	}
	
	
	private void readPreferences() {
		
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
		
		if (sharedPreferences.contains(SHARK_OWNER_ID))
			sharkOwnerID = Optional.of(Objects.requireNonNull(sharedPreferences.getString(SHARK_OWNER_ID, null)));
		else
			sharkOwnerID = Optional.empty();
		
		if (sharedPreferences.contains(USER_ID))
			userID = Optional.of(sharedPreferences.getLong(USER_ID, 0));
		else
			userID = Optional.empty();
		
		if (sharedPreferences.contains(USERNAME))
			username = Optional.of(Objects.requireNonNull(sharedPreferences.getString(USERNAME, null)));
		else
			username = Optional.empty();
		
	}
	
	private boolean generatePreferences() {
		
		if (!sharkOwnerID.isPresent())
			sharkOwnerID = Optional.of(ASAP.createUniqueID());
		
		if (!userID.isPresent())
			userID = Optional.of(new Random().nextLong());
		
		if (!username.isPresent()) {
			
			setContentView(R.layout.activity_initial);
			return false;
			
		}
		
		writePreferences();
		return true;
		
	}
	
	public void onUsernameConfirm(View view) {
		
		if (username.isPresent())
			return;
		
		EditText editText = findViewById(R.id.usernameText);
		
		String name = editText.getText().toString();
		
		if (name.length() == 0)
			return;
		
		username = Optional.of(name);
		
		writePreferences();
		
		initialize();
		
	}
	
	@SuppressLint("ApplySharedPref")
	private void writePreferences() {
		
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		editor.putString(SHARK_OWNER_ID, sharkOwnerID.get());
		editor.putLong(USER_ID, userID.get());
		editor.putString(USERNAME, username.get());
		
		editor.commit();
		
	}
	
	private void createSharkPeer() throws AssertionError {
		
		File asapRootDir = Util.getASAPRootDirectory(this, ASAP_FOLDER_NAME, sharkOwnerID.get());
		
		sharkPeer = new SharkPeerFS(sharkOwnerID.get(), asapRootDir.getAbsolutePath());
		
	}
	
	private void createCommunicationFuture() throws SharkException {
		
		communicationFuture = CommunicationFactory.createCommunication(userID.get(), username.get(), sharkPeer);
		
	}
	
	private void startSharkPeer() throws IOException, SharkException {
		
		Collection<CharSequence> formats = new ArrayList<>();
		formats.add(Communication.ASAP_FORMAT);
		
		ASAPAndroidPeer.initializePeer(sharkOwnerID.get(), formats, ASAP_FOLDER_NAME, this);
		ASAPAndroidPeer asapAndroidPeer = ASAPAndroidPeer.startPeer(this);
		
		sharkPeer.start(asapAndroidPeer);
		
	}
	
	private void createComponents() throws IOException, ExecutionException, InterruptedException {
		
		MockCommunication communication = new MockCommunication();
		
		File appRootDir = getFilesDir();
		File gameStatesFolder = new File(appRootDir, "gamestates");
		File invitationsFile = new File(appRootDir, "invitations");
		
		if (!gameStatesFolder.isDirectory() && !gameStatesFolder.mkdirs())
			throw new IOException("Could not create necessary directories");
		
		if (invitationsFile.isDirectory())
			throw new IOException("invitations is a directory");
		
		System.out.println("Username: " + username.get());
		
		app.setCommunication(
			communication
		);
		app.setGameStateManager(GameStateManagerFactory.createGameStateManager(
			gameStatesFolder
		));
		app.setGameLogicManager(GameLogicManagerFactory.createGameLogicManager(
			app.getGameStateManager(),
			communication
		));
		app.setInvitationManager(InvitationManagerFactory.createInvitationManager(
			app.getGameStateManager(),
			communication,
			invitationsFile
		));
		
		app.setInitialized();
		
	}
	
}