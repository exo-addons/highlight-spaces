package org.exoplatform.highlight.spaces;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

public class HighlightedSpacesService {

  private List<String>                        highlightedSpaces;

  private final String                        HIGHLIGHED_SPACES = "exo.addons.highlightedSpaces";

  private SpaceService                        spaceService;

  private final ExoCache<String, List<Space>> highlightedSpacesCache;

  public HighlightedSpacesService(SpaceService spaceService, CacheService cacheService) {
    highlightedSpaces = new ArrayList<>();

    String highlightedSpacesProperty = System.getProperty(HIGHLIGHED_SPACES, "").trim();
    String[] spaceName = highlightedSpacesProperty.split(",");
    for (String space : spaceName) {
      if (!space.equals("")) {
        highlightedSpaces.add(space.trim());
      }
    }
    this.spaceService = spaceService;

    highlightedSpacesCache = cacheService.getCacheInstance(this.getClass().getName() + "Cache");

  }

  public List<Space> getUserHighlightedSpaces(String remoteUser) {

    List<Space> cachedResults = highlightedSpacesCache.get(remoteUser);
    if (cachedResults != null) {
      return cleanCachedResult(cachedResults);
    }
    List<Space> results = new ArrayList<>();

    for (String spaceName : highlightedSpaces) {
      Space space = spaceService.getSpaceByPrettyName(spaceName);
      if (space != null && spaceService.isMember(space, remoteUser)) {
        results.add(space);
      }
    }
    highlightedSpacesCache.put(remoteUser, results);
    return results;
  }

  public void invalidate(String target) {
    highlightedSpacesCache.remove(target);
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
