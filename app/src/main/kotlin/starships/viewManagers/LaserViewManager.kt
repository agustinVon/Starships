package starships.viewManagers

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.LaserCollider
import starships.spaceItems.Laser

class LaserViewManager(val laser:Laser, image:Image, height: Double, width:Double, angle:Double) {
    val collider = LaserCollider(height, width, angle, laser)
    val view = ImageView(image)
    init {
        collider.shape.layoutX = laser.position.x + 25.0
        collider.shape.layoutY = laser.position.y + 25.0
        view.layoutX = laser.position.x
        view.layoutY = laser.position.y
        view.rotate = Math.toDegrees(laser.direction.angle) - 90
    }
    fun calculateView() {
        collider.shape.layoutX = laser.position.x
        collider.shape.layoutY = laser.position.y
        collider.shape.rotate = Math.toDegrees(laser.direction.angle) - 90
        view.layoutX = laser.position.x
        view.layoutY = laser.position.y
        view.rotate = Math.toDegrees(laser.direction.angle) - 90
    }
}