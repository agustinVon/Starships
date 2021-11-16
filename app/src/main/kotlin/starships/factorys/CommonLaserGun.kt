package starships.factorys

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
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
@SerialName("Gun")
class LaserGun(private val speed: Double, private val cooldown: Int): Gun() {
    @Contextual
    @Transient
    private val cooldownSpan = cooldown.milliseconds
    @Contextual
    @Transient
    private var lastShotTime = DateTime.now();

    override fun shoot(starship: Starship): List<Laser> {
        val currentShot = DateTime.now()
        if(currentShot - lastShotTime >= cooldownSpan){
            val imageLoader = ImageLoader()
            val laserImage = imageLoader.loadFromResources("laser.png", 30.0, 30.0)
            val starshipCenter = starship.position.add(Vector2.vector(21.0, 21.0))
            val laserPosition = starshipCenter.subtract(starship.direction.multiply(40.0)) //TODO add width field to spaceship
            lastShotTime = currentShot
            val laser = Laser(laserPosition, starship.direction.multiply(-1.0), speed, ImageView(laserImage), 10.0, 5.0, starship::addScore, starship.id)
            val list = ArrayList<Laser>()
            list.add(laser)
            return list
        } else throw GunIsInCoolDownException()
    }
}
