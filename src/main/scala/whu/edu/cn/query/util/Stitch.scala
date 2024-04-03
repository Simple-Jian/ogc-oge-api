package whu.edu.cn.query.util

import geotrellis.layer.{LayoutDefinition, SpatialKey}
import geotrellis.layer.stitch.TileLayoutStitcher
import geotrellis.raster.{Raster, Tile}

object Stitch {
  def stitchArrayTiles(tileLayerArray: Array[(SpatialKey, Tile)], layout: LayoutDefinition): Raster[Tile] = {
    val (tile, (kx, ky), (offsx, offsy)) = TileLayoutStitcher.stitch(tileLayerArray)
    val mapTransform = layout.mapTransform
    val nwTileEx = mapTransform(kx, ky)
    val base = nwTileEx.southEast
    val (ulx, uly) = (base.getX - offsx.toDouble * layout.cellwidth, base.getY + offsy * layout.cellheight)
    Raster(tile, geotrellis.vector.Extent(ulx, uly - tile.rows * layout.cellheight, ulx + tile.cols * layout.cellwidth, uly))
  }
}
