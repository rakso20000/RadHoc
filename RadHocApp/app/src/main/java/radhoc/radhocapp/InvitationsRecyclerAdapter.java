package radhoc.radhocapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import radhoc.invitations.Invitation;

public class InvitationsRecyclerAdapter extends RecyclerView.Adapter<InvitationsRecyclerAdapter.ViewHolder> {
	
	private List<Invitation> invitations;
	
	public InvitationsRecyclerAdapter(List<Invitation> invitations) {
		
		this.invitations = invitations;
		
	}
	
	public void updateInvitations(List<Invitation> invitations) {
		
		this.invitations = invitations;
		notifyDataSetChanged();
		
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		
		private final ImageButton acceptButton;
		private final ImageButton declineButton;
		private final TextView gameTypeText;
		private final TextView opponentNameText;
		
		public ViewHolder(@NonNull View view) {
			super(view);
			
			acceptButton = view.findViewById(R.id.invitation_accept_button);
			declineButton = view.findViewById(R.id.invitation_decline_button);
			gameTypeText = view.findViewById(R.id.invitation_gametype);
			opponentNameText = view.findViewById(R.id.invitation_opponentname);
			
		}
		
	}
	
	@NonNull
	@Override
	public InvitationsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_item, parent, false);
		
		return new ViewHolder(itemView);
		
	}
	
	@Override
	public void onBindViewHolder(@NonNull InvitationsRecyclerAdapter.ViewHolder holder, int position) {
		
		final Invitation invitation = invitations.get(position);
		
		holder.acceptButton.setOnClickListener(view -> invitation.accept());
		holder.declineButton.setOnClickListener(view -> invitation.decline());
		holder.gameTypeText.setText(invitation.getGameType().toString());
		holder.opponentNameText.setText(invitation.getOpponentName());
		
	}
	
	@Override
	public int getItemCount() {
		
		return invitations.size();
		
	}
	
}