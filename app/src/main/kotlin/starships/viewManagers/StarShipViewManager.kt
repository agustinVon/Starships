package starships.viewManagers

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.StarShipCollider
import starships.spaceItems.Starship

class StarShipViewManager(val starship:Starship, image:Image, height: Double, width:Double, angle:Double) {
    val view = ImageView(image)
    val collider = StarShipCollider(height, width, angle, starship)
    init {
        collider.shape.layoutX = starship.position.x + 25.0
        collider.shape.layoutY = starship.position.y + 25.0
        view.layoutX = starship.position.x
        view.layoutY = starship.position.y
        view.rotate = Math.toDegrees(starship.direction.angle) - 90
    }
    fun calculateView() {
        collider.shape.layoutX = starship.position.x
        collider.shape.layoutY = starship.position.y
        collider.shape.rotate = Math.toDegrees(starship.direction.angle) - 90
        view.layoutX = starship.position.x
        view.layoutY = starship.position.y
        view.rotate = Math.toDegrees(starship.direction.angle) - 90
    }
}