package radhoc.radhocapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class IntegrationTests {
	
	@Rule
	public ActivityScenarioRule<InitialActivity> activityScenarioRule = new ActivityScenarioRule<>(InitialActivity.class);
	
	@Test
	public void navigateInvitations() {
		
		//requires having initialized the app with a username
		
		onView(withId(R.id.title_text)).check(matches(withText("RadHoc"))).check(matches(isDisplayed()));
		onView(withId(R.id.back_button)).check(matches(withEffectiveVisibility(Visibility.GONE)));
		onView(withId(R.id.next_button)).check(matches(isDisplayed())).perform(click());
		
		onView(withId(R.id.title_text)).check(matches(withText("Invitations"))).check(matches(isDisplayed()));
		onView(withId(R.id.back_button)).check(matches(isDisplayed()));
		onView(withId(R.id.next_button)).check(matches(isDisplayed())).perform(click());
		
		onView(withId(R.id.title_text)).check(matches(withText("Send Invite"))).check(matches(isDisplayed()));
		onView(withId(R.id.back_button)).check(matches(isDisplayed()));
		onView(withId(R.id.next_button)).check(matches(withEffectiveVisibility(Visibility.GONE)));
		onView(withId(R.id.tic_tac_toe_radio)).check(matches(isChecked())).check(matches(isDisplayed()));
		onView(withId(R.id.rock_paper_scissors_radio)).check(matches(isNotChecked())).check(matches(isDisplayed()))
			.perform(click()).check(matches(isChecked())).check(matches(isDisplayed()));
		onView(withId(R.id.tic_tac_toe_radio)).check(matches(isNotChecked())).check(matches(isDisplayed()));
		onView(withId(R.id.sendInvitationButton)).check(matches(isDisplayed())).perform(click());
		
		onView(withId(R.id.title_text)).check(matches(withText("Invitations"))).check(matches(isDisplayed()));
		onView(withId(R.id.back_button)).check(matches(isDisplayed())).perform(click());
		
		onView(withId(R.id.title_text)).check(matches(withText("RadHoc"))).check(matches(isDisplayed()));
		
	}
	
	@Test
	public void playTicTacToe() {
		
		//requires exactly one playable GameStateTicTacToe with ownShape CROSS
		
		onView(allOf(withId(R.id.gamestate_play_button), withText("Play"), withParent(allOf(withId(R.id.gamestate_item_inner), withChild(withText("TicTacToe")))))).perform(click());
		
		onView(withId(R.id.field10)).check(matches(withText(""))).perform(click()).check(matches(withText("X")));
		onView(withId(R.id.field11)).check(matches(withText(""))).perform(click()).check(matches(withText("")));
		
	}
	
}