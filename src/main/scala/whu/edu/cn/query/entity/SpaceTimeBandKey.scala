package whu.edu.cn.query.entity

import geotrellis.layer.SpaceTimeKey

/**
 * Add measurement(band) info to Geotrellis SpaceTimeKey.
 *
 */
case class SpaceTimeBandKey (_spaceTimeKey: SpaceTimeKey, _measurementName: String){
  val spaceTimeKey = _spaceTimeKey
  val measurementName = _measurementName
}
