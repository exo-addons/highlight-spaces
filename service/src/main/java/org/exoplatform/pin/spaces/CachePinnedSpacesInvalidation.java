package org.exoplatform.pin.spaces;

import org.exoplatform.social.core.space.SpaceListenerPlugin;
import org.exoplatform.social.core.space.spi.SpaceLifeCycleEvent;

public class CachePinnedSpacesInvalidation extends SpaceListenerPlugin {

  private PinnedSpacesService pinnedSpacesService;

  public CachePinnedSpacesInvalidation(PinnedSpacesService pinnedSpacesService) {
    this.pinnedSpacesService = pinnedSpacesService;
  }

  @Override
  public void spaceCreated(SpaceLifeCycleEvent event) {
    pinnedSpacesService.invalidate(event.getTarget());
  }

  @Override
  public void spaceRemoved(SpaceLifeCycleEvent event) {
  }

  @Override
  public void applicationAdded(SpaceLifeCycleEvent event) {
  }

  @Override
  public void applicationRemoved(SpaceLifeCycleEvent event) {
  }

  @Override
  public void applicationActivated(SpaceLifeCycleEvent event) {
  }

  @Override
  public void applicationDeactivated(SpaceLifeCycleEvent event) {
  }

  @Override
  public void joined(SpaceLifeCycleEvent event) {
    pinnedSpacesService.invalidate(event.getTarget());
  }

  @Override
  public void left(SpaceLifeCycleEvent event) {
    pinnedSpacesService.invalidate(event.getTarget());
  }

  @Override
  public void grantedLead(SpaceLifeCycleEvent event) {
  }

  @Override
  public void revokedLead(SpaceLifeCycleEvent event) {
  }

  @Override
  public void spaceRenamed(SpaceLifeCycleEvent event) {
  }

  @Override
  public void spaceDescriptionEdited(SpaceLifeCycleEvent event) {
  }

  @Override
  public void spaceAvatarEdited(SpaceLifeCycleEvent event) {
  }

  @Override
  public void spaceAccessEdited(SpaceLifeCycleEvent event) {
  }

  @Override
  public void addInvitedUser(SpaceLifeCycleEvent event) {
  }

  @Override
  public void addPendingUser(SpaceLifeCycleEvent event) {
  }

  @Override
  public void spaceBannerEdited(SpaceLifeCycleEvent event) {
  }
}
