package starships.viewManagers

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.AsteroidCollider
import starships.spaceItems.Asteroid

class AsteroidViewManager(val asteroid: Asteroid, image: Image, height: Double, width:Double, angle:Double) {
    val view = ImageView(image)
    val collider = AsteroidCollider(height, width, angle, asteroid)
    init {
        collider.shape.layoutX = asteroid.position.x + 25.0
        collider.shape.layoutY = asteroid.position.y + 25.0
        view.layoutX = asteroid.position.x
        view.layoutY = asteroid.position.y
        view.rotate = Math.toDegrees(asteroid.direction.angle) - 90
    }
    fun calculateView() {
        collider.shape.layoutX = asteroid.position.x
        collider.shape.layoutY = asteroid.position.y
        collider.shape.rotate = Math.toDegrees(asteroid.direction.angle) - 90
        view.layoutX = asteroid.position.x
        view.layoutY = asteroid.position.y
        view.rotate = Math.toDegrees(asteroid.direction.angle) - 90
    }
}