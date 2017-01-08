package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.map.event.SyncMarkers;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class MapObject extends EventBusObject {

  /**
   * List of marker drawn over this object.
   */
  private final List<Marker> markers;

  private Coordinate coordinate;

  public MapObject(EventBus eventBus, Coordinate coordinate, @InitialMarkers List<Marker> markers) {
    super(eventBus);
    this.markers = markers;
    this.coordinate = coordinate;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  /**
   * Sets the coordinate of this object to the new coordinate Subclasses are responsible for posting
   * changes. This method is purposely set to protected to enforce the above rule.
   */
  protected void setCoordinate(Coordinate newCoordinate) {
    coordinate = newCoordinate;
  }

  public void addMarker(Marker marker) {
    markers.add(marker);
    post(new SyncMarkers(this, markers));
  }

  public void removeMarker(Marker marker) {
    markers.remove(marker);
    post(new SyncMarkers(this, markers));
  }

  /**
   * Enables the visitor pattern for selection.
   *
   * <p>I can't believe OOD actually taught me something useful.
   */
  public abstract void select(MapState mapState);

  /**
   * Enables the visitor pattern for highlight.
   */
  public abstract void highlight(MapState mapState);

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface InitialMarkers {

  }
}
