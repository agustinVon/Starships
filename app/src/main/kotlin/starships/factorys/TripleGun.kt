package starships.factorys

import com.soywiz.klock.DateTime
import com.soywiz.klock.milliseconds
import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import starships.spaceItems.Laser
import starships.spaceItems.Starship

@Serializable
@SerialName("TripleGun")
class TripleGun(private val speed: Double): Gun() {
    @Contextual
    @Transient
    var lastShotTime = DateTime.now();
    override fun shoot(starship: Starship): List<Laser> {
        val currentShot = DateTime.now()
        if(currentShot - lastShotTime >= 500.milliseconds){
            val imageLoader = ImageLoader()
            val laserImage = imageLoader.loadFromResources("laser.png", 30.0, 30.0)
            val starshipCenter = starship.position.add(Vector2.vector(21.0, 21.0))

            val directionleft = starship.direction.rotate(Math.toRadians(10.0))
            val directionRight = starship.direction.rotate(Math.toRadians(-10.0))

            val laserPosition1 = starshipCenter.subtract(starship.direction.multiply(40.0)) //TODO add width field to spaceship
            val laserPosition2 = starshipCenter.subtract(directionleft.multiply(40.0))
            val laserPosition3 = starshipCenter.subtract(directionRight.multiply(40.0))

            lastShotTime = currentShot
            val laser = Laser(laserPosition1, starship.direction.multiply(-1.0), speed, ImageView(laserImage), 10.0, 5.0, starship::addScore, starship.id)
            val laser2  = Laser(laserPosition2, directionleft.multiply(-1.0), speed, ImageView(laserImage), 10.0, 5.0, starship::addScore, starship.id)
            val laser3  = Laser(laserPosition3, directionRight.multiply(-1.0), speed, ImageView(laserImage), 10.0, 5.0, starship::addScore, starship.id)

            val list = ArrayList<Laser>()
            list.add(laser)
            list.add(laser2)
            list.add(laser3)
            return list

        } else throw GunIsInCoolDownException()
    }
}