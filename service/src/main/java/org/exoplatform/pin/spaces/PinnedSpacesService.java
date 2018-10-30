package org.exoplatform.pin.spaces;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class PinnedSpacesService {

  private List<String>                        pinnedSpaces;

  private final String                        PINNED_SPACES = "exo.addons.pinnedSpaces";

  private SpaceService                        spaceService;

  private final ExoCache<String, List<Space>> pinnedSpacesCache;

  public PinnedSpacesService(SpaceService spaceService, CacheService cacheService) {
    pinnedSpaces = new ArrayList<>();

    String pinnedSpacesProperty = System.getProperty(PINNED_SPACES, "").trim();
    String[] spaceName = pinnedSpacesProperty.split(",");
    for (String space : spaceName) {
      if (!space.equals("")) {
        pinnedSpaces.add(space.trim());
      }
    }
    this.spaceService = spaceService;

    pinnedSpacesCache = cacheService.getCacheInstance(this.getClass().getName() + "Cache");

  }

  public List<Space> getUserPinnedSpaces(String remoteUser) {

    List<Space> cachedResults = pinnedSpacesCache.get(remoteUser);
    if (cachedResults != null) {
      return cleanCachedResult(cachedResults);
    }
    List<Space> results = new ArrayList<>();

    for (String spaceName : pinnedSpaces) {
      Space space = spaceService.getSpaceByPrettyName(spaceName);
      if (space != null && spaceService.isMember(space, remoteUser)) {
        results.add(space);
      }
    }
    pinnedSpacesCache.put(remoteUser, results);
    return results;
  }

  public void invalidate(String target) {
    pinnedSpacesCache.remove(target);
  }
  
  /* We use this method in order to clean removed spaces since the spaceRemoved event target is null and not containing the remote user removing the space*/
  private List<Space> cleanCachedResult (List<Space> cachedResults) {
    List<Space> cleanedCachedResults = new ArrayList<>();
    for (Space cachedResult : cachedResults) {
      Space space = spaceService.getSpaceByPrettyName(cachedResult.getPrettyName());
      if (space != null) {
        cleanedCachedResults.add(space);
      }
    }
    return cleanedCachedResults;
  }
}
